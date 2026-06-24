
package nl.inholland.codegen.bankingapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.inholland.codegen.bankingapp.dtos.LoginRequestDTO;
import nl.inholland.codegen.bankingapp.dtos.LoginResponseDTO;
import nl.inholland.codegen.bankingapp.dtos.RegisterRequestDTO;
import nl.inholland.codegen.bankingapp.dtos.UserResponseDTO;
import nl.inholland.codegen.bankingapp.exceptions.AuthenticationException;
import nl.inholland.codegen.bankingapp.exceptions.BadRequestException;
import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;
import nl.inholland.codegen.bankingapp.security.JwtService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AccountService accountService;
    @Mock private IbanGeneratorService ibanGeneratorService;

    @InjectMocks private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("hashedPass");
        existingUser.setRole(User.Role.CUSTOMER);
    }

    // ---------- login ----------

    @Test
    void login_validCredentials_returnsTokenAndRole() {
        LoginRequestDTO request = new LoginRequestDTO("john@example.com", "password123");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken(existingUser)).thenReturn("jwt-token");

        LoginResponseDTO result = userService.login(request);

        assertEquals("jwt-token", result.token());
        assertEquals("CUSTOMER", result.role());
    }

    @Test
    void login_emailNotFound_throwsAuthenticationException() {
        LoginRequestDTO request = new LoginRequestDTO("missing@example.com", "password123");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.login(request));
    }

    @Test
    void login_wrongPassword_throwsAuthenticationException() {
        LoginRequestDTO request = new LoginRequestDTO("john@example.com", "wrongpass");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongpass", "hashedPass")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> userService.login(request));
    }

    // ---------- registerCustomer ----------

    @Test
    void registerCustomer_validRequest_createsUserAndAccount() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Jane", "Doe", "jane@example.com", "Password1", "123456789", "0612345678");

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByBsn("123456789")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1")).thenReturn("hashedPassword1");

        User savedUser = new User();
        savedUser.setUserId(2L);
        savedUser.setFirstName("Jane");
        savedUser.setLastName("Doe");
        savedUser.setEmail("jane@example.com");
        savedUser.setRole(User.Role.CUSTOMER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        when(ibanGeneratorService.generate()).thenReturn("NL91INHO0000000099");
        when(accountService.createAccount(savedUser, "NL91INHO0000000099")).thenReturn(new Account());

        UserResponseDTO result = userService.registerCustomer(request);

        assertEquals(2L, result.userId());
        assertEquals("Jane", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("jane@example.com", result.email());
        assertEquals(User.Role.CUSTOMER, result.role());
        verify(accountService).createAccount(savedUser, "NL91INHO0000000099");
    }

    @Test
    void registerCustomer_emailAlreadyInUse_throwsBadRequest() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Jane", "Doe", "john@example.com", "Password1", "123456789", "0612345678");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(BadRequestException.class, () -> userService.registerCustomer(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerCustomer_bsnAlreadyInUse_throwsBadRequest() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Jane", "Doe", "jane@example.com", "Password1", "123456789", "0612345678");

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByBsn("123456789")).thenReturn(Optional.of(existingUser));

        assertThrows(BadRequestException.class, () -> userService.registerCustomer(request));
        verify(userRepository, never()).save(any());
    }
}
