package nl.inholland.codegen.bankingapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.inholland.codegen.bankingapp.dtos.AccountDetailDto;
import nl.inholland.codegen.bankingapp.dtos.AccountSummaryDto;
import nl.inholland.codegen.bankingapp.dtos.CustomerLookupDto;
import nl.inholland.codegen.bankingapp.models.User;
import nl.inholland.codegen.bankingapp.services.AccountService;

@RestController @RequestMapping("/customers")
@Tag(name="Customer") @SecurityRequirement(name="Bearer Authentication")
public class CustomerController {
    private final AccountService accountService;
    public CustomerController(AccountService accountService) { this.accountService = accountService; }

    @GetMapping("/accounts")
    @Operation(summary="List my accounts")
    public ResponseEntity<List<AccountSummaryDto>> listAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.getAccountsForUser(user));
    }

    @GetMapping("/accounts/{id}")
    @Operation(summary="Get account detail")
    public ResponseEntity<AccountDetailDto> getAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(accountService.getAccountDetail(id, user));
    }

    @GetMapping("/search")
    @Operation(summary="Search customers by name")
    public ResponseEntity<List<CustomerLookupDto>> search(
            @RequestParam(required=false) String firstName,
            @RequestParam(required=false) String lastName) {
        return ResponseEntity.ok(accountService.searchByName(firstName, lastName));
    }
}
