package ra.ss10.entity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CreditCard {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    private Double spendingLimit;
    private Double amountSpent = 0.0;
    private String status = "active"; // "active", "inactive"
}
