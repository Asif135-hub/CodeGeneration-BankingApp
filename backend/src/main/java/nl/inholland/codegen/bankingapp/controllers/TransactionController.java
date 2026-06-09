package nl.inholland.codegen.bankingapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.inholland.codegen.bankingapp.dtos.*;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.services.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController @RequestMapping("/transactions")
@Tag(name="Transactions") @SecurityRequirement(name="Bearer Authentication")
public class TransactionController {
    private final AccountService accountService;
    public TransactionController(AccountService accountService) { this.accountService = accountService; }

    @PostMapping
    @Operation(summary="Transfer funds between accounts")
    public ResponseEntity<TransactionDto> transfer(@Valid @RequestBody TransferRequest req,
                                                    @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.transfer(req.fromIban(), req.toIban(), req.amount(), user));
    }

    @GetMapping
    @Operation(summary="List/filter transactions")
    public ResponseEntity<Page<TransactionDto>> list(
            @AuthenticationPrincipal User user,
            @RequestParam(required=false) String fromIban,
            @RequestParam(required=false) String toIban,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required=false) BigDecimal amount,
            @RequestParam(required=false) String amountOp,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="Descending") String sort) {
        return ResponseEntity.ok(accountService.getTransactions(user, fromIban, toIban, start, end, amount, amountOp, page, size, sort));
    }
}
