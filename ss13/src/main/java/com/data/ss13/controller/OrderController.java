package com.data.ss13.controller;

import com.data.ss13.model.dto.request.CheckoutRequest;
import com.data.ss13.model.dto.OrderDTO;
import com.data.ss13.model.entity.Order;
import com.data.ss13.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        orderService.checkout(checkoutRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders() {
        List<OrderDTO> orders = orderService.getUserOrders()
                .stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderDTO.fromEntity(order));
    }
}