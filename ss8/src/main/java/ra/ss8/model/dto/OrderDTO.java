package ra.ss8.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long customerId;
    private Long employeeId;
    private List<OrderDetailDTO> orderDetails;

    @Setter
    @Getter
    public static class OrderDetailDTO {
        // Getters and setters
        private Long dishId;
        private Integer quantity;

        // Constructors
        public OrderDetailDTO() {
        }

        public OrderDetailDTO(Long dishId, Integer quantity) {
            this.dishId = dishId;
            this.quantity = quantity;
        }

    }
}
