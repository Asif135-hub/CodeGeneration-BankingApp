package nl.inholland.codegen.bankingapp.controllers;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.inholland.codegen.bankingapp.dtos.AtmLoginRequestDto;
import nl.inholland.codegen.bankingapp.dtos.AtmTransactionRequestDto;
import nl.inholland.codegen.bankingapp.dtos.LoginResponseDTO;
import nl.inholland.codegen.bankingapp.dtos.TransactionFilterRequestDto;
import nl.inholland.codegen.bankingapp.models.Transaction;
import nl.inholland.codegen.bankingapp.services.AtmService;

@RestController
@RequestMapping("/atm")
@Tag(name = "ATM", description = "ATM operations – login, deposit, withdraw, transaction history")
public class AtmController {

    private final AtmService atmService;

    public AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    @PostMapping("/login")
    @Operation(summary = "ATM Login", description = "Authenticate at the ATM using email and password")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AtmLoginRequestDto request) {
        return ResponseEntity.ok(atmService.login(request.email(), request.password()));
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw", description = "Withdraw cash from an account via IBAN")
    public ResponseEntity<Map<String, String>> withdraw(@Valid @RequestBody AtmTransactionRequestDto request) {
        atmService.withdraw(request.iban(), request.amount());
        return ResponseEntity.ok(Map.of("message", "Withdrawal successful"));

    }

    @PostMapping("/deposit")
    @Operation(summary = "Deposit", description = "Deposit cash into an account via IBAN")
    public ResponseEntity<Map<String, String>> deposit(@Valid @RequestBody AtmTransactionRequestDto request) {
        atmService.deposit(request.iban(), request.amount());
        return ResponseEntity.ok(Map.of("message", "Deposit successful"));

    }

    @PostMapping("/transactions/search")
    @Operation(summary = "Search transactions", description = "Filter transactions by date range, amount, or IBAN")
    public ResponseEntity<Page<Transaction>> search(@RequestBody TransactionFilterRequestDto request) {
        return ResponseEntity.ok(atmService.filterTransactions(request));
    }
}
