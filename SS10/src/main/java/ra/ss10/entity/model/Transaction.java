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
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;

    private Double money;
    private String note;
    private String status;
    private LocalDateTime createdAt;
}
