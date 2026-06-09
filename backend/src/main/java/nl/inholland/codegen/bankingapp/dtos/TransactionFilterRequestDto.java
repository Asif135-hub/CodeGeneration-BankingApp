package nl.inholland.codegen.bankingapp.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionFilterRequestDto(
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal minAmount,
    BigDecimal maxAmount,
    String iban,
    Integer page,
    Integer size
) {}
