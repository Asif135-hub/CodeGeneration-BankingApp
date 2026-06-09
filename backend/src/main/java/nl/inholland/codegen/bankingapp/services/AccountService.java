package nl.inholland.codegen.bankingapp.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import nl.inholland.codegen.bankingapp.dtos.AccountDetailDto;
import nl.inholland.codegen.bankingapp.dtos.AccountSummaryDto;
import nl.inholland.codegen.bankingapp.dtos.CustomerLookupDto;
import nl.inholland.codegen.bankingapp.dtos.TransactionDto;
import nl.inholland.codegen.bankingapp.exceptions.BadRequestException;
import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.Transaction;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.AccountRepository;
import nl.inholland.codegen.bankingapp.repositories.TransactionRepository;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<AccountSummaryDto> getAccountsForUser(User user) {
        return accountRepository.findByUser(user).stream()
            .map(a -> new AccountSummaryDto(a.getId(), a.getIban(), a.getBalance() , a.getDailyLimit(), a.getAbsoluteLimit()))
            .toList();
    }

    @Transactional
    public Account createAccount(User user, String iban) {

        if (accountRepository.findByIban(iban).isPresent()) {
            throw new BadRequestException("IBAN already in use");
        }

        Account account = new Account();
        account.setIban(iban);
        account.setBalance(BigDecimal.ZERO);
        account.setDailyLimit(BigDecimal.valueOf(500));
        account.setAbsoluteLimit(BigDecimal.valueOf(2000));
        account.setUser(user);

        return accountRepository.save(account);
    }

    public AccountDetailDto getAccountDetail(Long accountId, User requestingUser) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new BadRequestException("Account not found"));
        if (requestingUser.getRole() == User.Role.CUSTOMER &&
            !account.getUser().getUserId().equals(requestingUser.getUserId())) {
            throw new BadRequestException("Access denied");
        }
        return toDetailDto(account);
    }

    public List<CustomerLookupDto> searchByName(String firstName, String lastName) {

        boolean hasFirstName = firstName != null && !firstName.trim().isEmpty();
        boolean hasLastName = lastName != null && !lastName.trim().isEmpty();

        List<User> users;

        if (hasFirstName && hasLastName) {

            users = userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                    firstName.trim(),
                    lastName.trim()
            );

        } else if (hasFirstName) {

            users = userRepository.findByFirstNameContainingIgnoreCase(firstName.trim());

        } else if (hasLastName) {

            users = userRepository.findByLastNameContainingIgnoreCase(lastName.trim());

        } else {

            users = new ArrayList<>();
        }

        List<CustomerLookupDto> result = new ArrayList<>();

        for (User u : users) {
            accountRepository.findByUser(u).forEach(acc ->
                    result.add(new CustomerLookupDto(
                            u.getUserId(),
                            u.getFirstName(),
                            u.getLastName(),
                            acc.getIban()
                    ))
            );
        }

        return result;
    }

    @Transactional
    public TransactionDto transfer(String fromIban, String toIban, BigDecimal amount, User initiator) {
        Account from = accountRepository.findByIban(fromIban)
            .orElseThrow(() -> new BadRequestException("Source account not found"));
        Account to = accountRepository.findByIban(toIban)
            .orElseThrow(() -> new BadRequestException("Destination account not found"));

        if (initiator.getRole() == User.Role.CUSTOMER &&
            !from.getUser().getUserId().equals(initiator.getUserId())) {
            throw new BadRequestException("You can only transfer from your own account");
        }

        validateTransferLimits(from, amount);

        if (from.getBalance().subtract(amount).compareTo(from.getAbsoluteLimit()) < 0) {
            throw new BadRequestException("Transfer would exceed absolute limit");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        updateDailyTracking(from, amount);

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction tx = new Transaction();
        tx.setFromIban(fromIban);
        tx.setToIban(toIban);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setType("TRANSFER");
        tx.setInitiatedBy(initiator.getEmail());
        transactionRepository.save(tx);

        return toTransactionDto(tx);
    }

    public Page<TransactionDto> getTransactions(User user, String fromIban, String toIban,
            LocalDate start, LocalDate end, BigDecimal amount, String amountOp,
            int page, int pageSize, String sort) {

        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (user.getRole() == User.Role.CUSTOMER) {
                List<String> ibans = accountRepository.findByUser(user)
                    .stream().map(Account::getIban).toList();
                predicates.add(cb.or(
                    root.get("fromIban").in(ibans),
                    root.get("toIban").in(ibans)
                ));
            }

            if (fromIban != null) predicates.add(cb.equal(root.get("fromIban"), fromIban));
            if (toIban != null)   predicates.add(cb.equal(root.get("toIban"), toIban));
            if (start != null)    predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), start.atStartOfDay()));
            if (end != null)      predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), end.atTime(23,59,59)));
            if (amount != null && amountOp != null) {
                predicates.add(switch (amountOp) {
                    case "GreaterThan" -> cb.greaterThan(root.get("amount"), amount);
                    case "LessThan"    -> cb.lessThan(root.get("amount"), amount);
                    default            -> cb.equal(root.get("amount"), amount);
                });
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sortObj = "Ascending".equals(sort)
            ? Sort.by("timestamp").ascending()
            : Sort.by("timestamp").descending();

        return transactionRepository.findAll(spec, PageRequest.of(page, pageSize, sortObj))
            .map(this::toTransactionDto);
    }

    private void validateTransferLimits(Account account, BigDecimal amount) {
        resetDailyIfNeeded(account);
        BigDecimal newDaily = account.getDailyTransferredAmount().add(amount);
        if (newDaily.compareTo(account.getDailyLimit()) > 0)
            throw new BadRequestException("Daily transfer limit exceeded");
    }

    private void resetDailyIfNeeded(Account account) {
        if (account.getLastTransferDate() == null || !account.getLastTransferDate().equals(LocalDate.now())) {
            account.setDailyTransferredAmount(BigDecimal.ZERO);
            account.setLastTransferDate(LocalDate.now());
        }
    }

    private void updateDailyTracking(Account account, BigDecimal amount) {
        account.setDailyTransferredAmount(account.getDailyTransferredAmount().add(amount));
        account.setLastTransferDate(LocalDate.now());
    }

    private AccountDetailDto toDetailDto(Account a) {
        return new AccountDetailDto(a.getId(), a.getIban(), a.getBalance(), a.getDailyLimit(), a.getAbsoluteLimit(), a.getUser().getUserId());
    }

    public TransactionDto toTransactionDto(Transaction t) {
        return new TransactionDto(t.getId(), t.getFromIban(), t.getToIban(), t.getAmount(), t.getTimestamp(), t.getType(), t.getInitiatedBy());
    }


}
