package ra.ss14.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TicketResponseDTO {
    private Long id;
    private String seatNumber;
    private BigDecimal price;
    private LocalDateTime bookingTime;
    private Long showtimeId;
    private String username;
}