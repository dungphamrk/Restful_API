package ra.ss10.entity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransactionCredit {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_receiver_id")
    private Account accountReceiver;

    @ManyToOne
    @JoinColumn(name = "credit_card_sender_id")
    private CreditCard creditCardSender;

    private String note;
    private Double money;
    private String status; // "thành công", "thất bại", "đang chờ xử lý"
    private LocalDateTime createdAt;
    private String failureReason;
}
