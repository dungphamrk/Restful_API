package ra.ss14.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private ShowTime showTime;

    @Column(nullable = false, length = 3, columnDefinition = "CHAR")
    private String seatNumber;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2)")
    private BigDecimal price;
}