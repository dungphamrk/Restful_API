package ra.ss14.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TicketRequestDTO {
    @NotNull(message = "ShowTimeId must not be null")
    private Long showtimeId;

    @NotBlank(message = "SeatNumber can't not be empty")
    private String seatNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}