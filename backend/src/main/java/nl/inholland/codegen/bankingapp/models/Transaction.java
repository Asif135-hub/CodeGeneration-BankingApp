package nl.inholland.codegen.bankingapp.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="transactions") @Data @NoArgsConstructor
public class Transaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String fromIban;
    private String toIban;
    @Column(nullable=false) private BigDecimal amount;
    @Column(nullable=false) private LocalDateTime timestamp;
    @Column(nullable=false) private String type;
    private String initiatedBy;
}
