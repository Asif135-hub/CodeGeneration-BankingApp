package nl.inholland.codegen.bankingapp.services;

import java.math.BigDecimal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import nl.inholland.codegen.bankingapp.dtos.LoginRequestDTO;
import nl.inholland.codegen.bankingapp.dtos.LoginResponseDTO;
import nl.inholland.codegen.bankingapp.dtos.RegisterRequestDTO;
import nl.inholland.codegen.bankingapp.dtos.UserResponseDTO;
import nl.inholland.codegen.bankingapp.exceptions.AuthenticationException;
import nl.inholland.codegen.bankingapp.exceptions.BadRequestException;
import nl.inholland.codegen.bankingapp.models.Account;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.repositories.AccountRepository;
import nl.inholland.codegen.bankingapp.repositories.UserRepository;
import nl.inholland.codegen.bankingapp.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountService accountService;

    public UserService(UserRepository userRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AccountService accountService
            ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accountService = accountService;

    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }

        return new LoginResponseDTO(jwtService.generateToken(user), user.getRole().name());
    }

    public UserResponseDTO registerCustomer(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }
        if (userRepository.findByBsn(request.bsn()).isPresent()) {
            throw new BadRequestException("BSN is already in use");
        }

        if (accountRepository.findByIban(request.iban()).isPresent()) {
            throw new BadRequestException("IBAN already in use");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBsn(request.bsn());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(User.Role.CUSTOMER);
        user.setStatus(User.CustomerStatus.APPROVED);

        User savedUser = userRepository.save(user);

       accountService.createAccount(savedUser, request.iban());

        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole());
    }
}