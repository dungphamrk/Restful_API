package ra.ss8.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss8.model.Order;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.model.dto.OrderDTO;
import ra.ss8.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        ApiResponse<Order> response = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        ApiResponse<List<Order>> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }
}