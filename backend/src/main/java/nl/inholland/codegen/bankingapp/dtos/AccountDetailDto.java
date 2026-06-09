package nl.inholland.codegen.bankingapp.dtos;
import java.math.BigDecimal;
public record AccountDetailDto(Long id, String iban, BigDecimal balance, BigDecimal dailyLimit, BigDecimal absoluteLimit, Long userId) {}
