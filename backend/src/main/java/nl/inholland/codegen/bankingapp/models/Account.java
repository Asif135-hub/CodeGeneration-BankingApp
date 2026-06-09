package nl.inholland.codegen.bankingapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String iban;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal dailyLimit;

    @Column(nullable = false)
    private BigDecimal absoluteLimit;

    @Column(nullable = false)
    private BigDecimal dailyTransferredAmount = BigDecimal.ZERO;

    private LocalDate lastTransferDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
