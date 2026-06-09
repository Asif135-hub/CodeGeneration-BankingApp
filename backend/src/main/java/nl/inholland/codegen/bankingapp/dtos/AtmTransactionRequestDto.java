package nl.inholland.codegen.bankingapp.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AtmTransactionRequestDto(
    @NotBlank String iban,
    @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount
) {}
