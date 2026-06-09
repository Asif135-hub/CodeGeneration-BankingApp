package nl.inholland.codegen.bankingapp.dtos;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record TransactionDto(Long id, String fromIban, String toIban, BigDecimal amount, LocalDateTime timestamp, String type, String initiatedBy) {}
