package nl.inholland.codegen.bankingapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.inholland.codegen.bankingapp.dtos.LoginResponseDTO;
import nl.inholland.codegen.bankingapp.exceptions.AuthenticationException;
import nl.inholland.codegen.bankingapp.exceptions.BadRequestException;
import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.AccountRepository;
import nl.inholland.codegen.bankingapp.repositories.TransactionRepository;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;
import nl.inholland.codegen.bankingapp.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AtmServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AtmService atmService;

    private User user;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("john@example.com");
        user.setPassword("hashedPass");
        user.setRole(User.Role.CUSTOMER);

        account = new Account();
        account.setId(1L);
        account.setIban("NL91INHO0000000001");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setDailyLimit(BigDecimal.valueOf(500));
        account.setAbsoluteLimit(BigDecimal.valueOf(0));
        account.setDailyTransferredAmount(BigDecimal.ZERO);
        account.setLastTransferDate(LocalDate.now());
        account.setUser(user);
    }

    // ---------- login ----------

    @Test
    void login_validCredentials_returnsToken() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginResponseDTO result = atmService.login("john@example.com", "password123");

        assertEquals("jwt-token", result.token());
        assertEquals("CUSTOMER", result.role());
    }

    @Test
    void login_unknownEmail_throwsAuthenticationException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class,
                () -> atmService.login("unknown@example.com", "password123"));
    }

    @Test
    void login_wrongPassword_throwsAuthenticationException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashedPass")).thenReturn(false);

        assertThrows(AuthenticationException.class,
                () -> atmService.login("john@example.com", "wrongpass"));
    }

    // ---------- withdraw ----------

    @Test
    void withdraw_validAmount_decreasesBalanceAndSavesTransaction() {
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        atmService.withdraw("NL91INHO0000000001", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(900), account.getBalance());
        assertEquals(BigDecimal.valueOf(100), account.getDailyTransferredAmount());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any());
    }

    @Test
    void withdraw_accountNotFound_throwsBadRequest() {
        when(accountRepository.findByIban("INVALID")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> atmService.withdraw("INVALID", BigDecimal.valueOf(100)));
    }

    @Test
    void withdraw_amountExceedsAbsoluteLimit_throwsBadRequest() {
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class,
                () -> atmService.withdraw("NL91INHO0000000001", BigDecimal.valueOf(2500)));
    }

    @Test
    void withdraw_exceedsDailyLimit_throwsBadRequest() {
        account.setDailyTransferredAmount(BigDecimal.valueOf(450));
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        // 450 + 100 = 550 > dailyLimit(500)
        assertThrows(BadRequestException.class,
                () -> atmService.withdraw("NL91INHO0000000001", BigDecimal.valueOf(100)));
    }

    @Test
    void withdraw_insufficientBalance_throwsBadRequest() {
        account.setBalance(BigDecimal.valueOf(50));
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class,
                () -> atmService.withdraw("NL91INHO0000000001", BigDecimal.valueOf(100)));
    }

    @Test
    void withdraw_dailyTrackingResetsOnNewDay() {
        account.setLastTransferDate(LocalDate.now().minusDays(1));
        account.setDailyTransferredAmount(BigDecimal.valueOf(490));
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        // would exceed daily limit if not reset (490+100=590>500), but date is stale so resets to 0 first
        atmService.withdraw("NL91INHO0000000001", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), account.getDailyTransferredAmount());
        assertEquals(LocalDate.now(), account.getLastTransferDate());
    }

    // ---------- deposit ----------

    @Test
    void deposit_validAmount_increasesBalanceAndSavesTransaction() {
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        atmService.deposit("NL91INHO0000000001", BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(1200), account.getBalance());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any());
    }

    @Test
    void deposit_accountNotFound_throwsBadRequest() {
        when(accountRepository.findByIban("INVALID")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> atmService.deposit("INVALID", BigDecimal.valueOf(100)));
    }

    @Test
    void deposit_amountExceedsAbsoluteLimit_throwsBadRequest() {
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class,
                () -> atmService.deposit("NL91INHO0000000001", BigDecimal.valueOf(2500)));
    }

    @Test
    void deposit_exceedsDailyLimit_throwsBadRequest() {
        account.setDailyTransferredAmount(BigDecimal.valueOf(450));
        when(accountRepository.findByIban("NL91INHO0000000001")).thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class,
                () -> atmService.deposit("NL91INHO0000000001", BigDecimal.valueOf(100)));
    }
}