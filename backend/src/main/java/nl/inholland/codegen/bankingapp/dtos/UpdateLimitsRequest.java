package nl.inholland.codegen.bankingapp.dtos;
import java.math.BigDecimal;
public record UpdateLimitsRequest(BigDecimal dailyLimit, BigDecimal absoluteLimit) {}
