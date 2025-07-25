package com.data.ss13.model.dto.request;


import com.data.ss13.model.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String receiver;
    private String address;
    private String phoneNumber;
    private Double totalMoney;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private UUID userId;

    public static OrderDTO fromEntity(com.data.ss13.model.entity.Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .receiver(order.getReceiver())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .totalMoney(order.getTotalMoney())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .userId(order.getUser().getId())
                .build();
    }
}