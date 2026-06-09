package nl.inholland.codegen.bankingapp.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import nl.inholland.codegen.bankingapp.dtos.LoginResponseDTO;
import nl.inholland.codegen.bankingapp.dtos.TransactionFilterRequestDto;
import nl.inholland.codegen.bankingapp.exceptions.AuthenticationException;
import nl.inholland.codegen.bankingapp.exceptions.BadRequestException;
import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.Transaction;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.AccountRepository;
import nl.inholland.codegen.bankingapp.repositories.TransactionRepository;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;
import nl.inholland.codegen.bankingapp.security.JwtService;

@Service
public class AtmService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AtmService(UserRepository userRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    public LoginResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        return new LoginResponseDTO(jwtService.generateToken(user), user.getRole().name());
    }

    @Transactional
    public void withdraw(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new BadRequestException("Account not found"));

        validateLimits(account, amount);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        updateDailyLimit(account, amount);
        saveTransaction(account.getIban(), null, amount, "WITHDRAW");
        accountRepository.save(account);
    }

    @Transactional
    public void deposit(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new BadRequestException("Account not found"));

        validateLimits(account, amount);

        account.setBalance(account.getBalance().add(amount));
        updateDailyLimit(account, amount);
        saveTransaction(null, account.getIban(), amount, "DEPOSIT");
        accountRepository.save(account);
    }

    private void validateLimits(Account account, BigDecimal amount) {
        if (amount.compareTo(account.getAbsoluteLimit()) > 0) {
            throw new BadRequestException("Amount exceeds absolute limit");
        }
        resetDailyLimitIfNeeded(account);
        BigDecimal total = account.getDailyTransferredAmount().add(amount);
        if (total.compareTo(account.getDailyLimit()) > 0) {
            throw new BadRequestException("Daily limit exceeded");
        }
    }

    private void resetDailyLimitIfNeeded(Account account) {
        if (account.getLastTransferDate() == null ||
                !account.getLastTransferDate().equals(LocalDate.now())) {
            account.setDailyTransferredAmount(BigDecimal.ZERO);
            account.setLastTransferDate(LocalDate.now());
        }
    }

    private void updateDailyLimit(Account account, BigDecimal amount) {
        account.setDailyTransferredAmount(account.getDailyTransferredAmount().add(amount));
        account.setLastTransferDate(LocalDate.now());
    }

    private void saveTransaction(String fromIban, String toIban, BigDecimal amount, String type) {
        Transaction tx = new Transaction();
        tx.setFromIban(fromIban);
        tx.setToIban(toIban);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setType(type);
        transactionRepository.save(tx);
    }

    public Page<Transaction> filterTransactions(TransactionFilterRequestDto request) {
        int page = request.page() != null ? request.page() : 0;
        int size = request.size() != null ? request.size() : 10;

        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.startDate() != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), request.startDate()));
            if (request.endDate() != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), request.endDate()));
            if (request.minAmount() != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), request.minAmount()));
            if (request.maxAmount() != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), request.maxAmount()));
            if (request.iban() != null)
                predicates.add(cb.or(
                        cb.equal(root.get("fromIban"), request.iban()),
                        cb.equal(root.get("toIban"), request.iban())));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return transactionRepository.findAll(spec, PageRequest.of(page, size));
    }
}
