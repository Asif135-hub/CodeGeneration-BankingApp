package nl.inholland.codegen.bankingapp.dtos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
public record ApproveCustomerRequest(
    @NotNull @Positive BigDecimal dailyLimit,
    @NotNull @Positive BigDecimal absoluteLimit
) {}
