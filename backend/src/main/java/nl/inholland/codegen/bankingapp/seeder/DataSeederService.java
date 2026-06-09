package nl.inholland.codegen.bankingapp.seeder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.Transaction;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.AccountRepository;
import nl.inholland.codegen.bankingapp.repositories.TransactionRepository;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;

@Service
public class DataSeederService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency injection
    public DataSeederService(UserRepository u, AccountRepository a, TransactionRepository t, PasswordEncoder p) {
        this.userRepository = u;
        this.accountRepository = a;
        this.transactionRepository = t;
        this.passwordEncoder = p;
    }

    public void seed() {
        if (userRepository.count() > 0)
            return;

        User admin = createUser("Admin", "Bank", "admin@bank.com", "Admin1234", "000000001", "+31600000001",
                User.Role.EMPLOYEE, User.CustomerStatus.APPROVED);

        User john = createUser("John", "Doe", "john@test.com", "Secret1234", "123456789", "+31612345678",
                User.Role.CUSTOMER, User.CustomerStatus.APPROVED);
        Account johnChecking = createAccount("NL10INHO0000000001", BigDecimal.valueOf(1500), BigDecimal.valueOf(500),
                BigDecimal.valueOf(200), john);
        Account johnSavings = createAccount("NL10INHO0000000002", BigDecimal.valueOf(3000), BigDecimal.valueOf(500),
                BigDecimal.valueOf(200), john);

        User emma = createUser("Emma", "Smith", "emma@test.com", "Secret1234", "987654321", "+31698765432",
                User.Role.CUSTOMER, User.CustomerStatus.PENDING);

                // Transfers
        saveTransaction(johnChecking.getIban(), johnSavings.getIban(), BigDecimal.valueOf(200), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(10));

        saveTransaction(johnSavings.getIban(), johnChecking.getIban(), BigDecimal.valueOf(100), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(9));

        saveTransaction(johnChecking.getIban(), johnSavings.getIban(), BigDecimal.valueOf(75), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(8));

        saveTransaction(johnSavings.getIban(), johnChecking.getIban(), BigDecimal.valueOf(150), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(7));

        saveTransaction(johnChecking.getIban(), johnSavings.getIban(), BigDecimal.valueOf(50), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(6));

        saveTransaction(johnSavings.getIban(), johnChecking.getIban(), BigDecimal.valueOf(300), "TRANSFER",
                "john@test.com", LocalDateTime.now().minusDays(5));

        // Deposits
        saveTransaction(null, johnChecking.getIban(), BigDecimal.valueOf(500), "DEPOSIT",
                "john@test.com", LocalDateTime.now().minusDays(4));

        saveTransaction(null, johnSavings.getIban(), BigDecimal.valueOf(1000), "DEPOSIT",
                "john@test.com", LocalDateTime.now().minusDays(3));

        saveTransaction(null, johnChecking.getIban(), BigDecimal.valueOf(250), "DEPOSIT",
                "john@test.com", LocalDateTime.now().minusDays(2));

        // Withdrawals
        saveTransaction(johnChecking.getIban(), null, BigDecimal.valueOf(50), "WITHDRAW",
                "john@test.com", LocalDateTime.now().minusHours(20));

        saveTransaction(johnSavings.getIban(), null, BigDecimal.valueOf(100), "WITHDRAW",
                "john@test.com", LocalDateTime.now().minusHours(10));

        saveTransaction(johnChecking.getIban(), null, BigDecimal.valueOf(25), "WITHDRAW",
                "john@test.com", LocalDateTime.now().minusHours(1));
    }

    private User createUser(String fn, String ln, String email, String pw, String bsn, String phone, User.Role role,
            User.CustomerStatus status) {
        User u = new User();
        u.setFirstName(fn);
        u.setLastName(ln);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(pw));
        u.setBsn(bsn);
        u.setPhoneNumber(phone);
        u.setRole(role);
        u.setStatus(status);
        return userRepository.save(u);
    }

    private Account createAccount(String iban, BigDecimal bal, BigDecimal daily, BigDecimal absolute, User u) {
        Account a = new Account();
        a.setIban(iban);
        a.setBalance(bal);
        a.setDailyLimit(daily);
        a.setAbsoluteLimit(absolute);
        a.setUser(u);
        return accountRepository.save(a);
    }

    private void saveTransaction(String from, String to, BigDecimal amt, String type, String by, LocalDateTime ts) {
        Transaction t = new Transaction();
        t.setFromIban(from);
        t.setToIban(to);
        t.setAmount(amt);
        t.setType(type);
        t.setInitiatedBy(by);
        t.setTimestamp(ts);
        transactionRepository.save(t);
    }
}
