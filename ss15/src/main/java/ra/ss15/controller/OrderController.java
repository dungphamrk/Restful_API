package ra.ss15.controller;

import ra.ss15.model.dto.request.CreateOrderRequest;
import ra.ss15.model.dto.response.APIResponse;
import ra.ss15.model.entity.Order;
import ra.ss15.model.entity.OrderStatus;
import ra.ss15.secutiry.principal.CustomUserDetails;
import ra.ss15.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<Order>> createOrder(@RequestBody @Valid CreateOrderRequest request,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Order order = orderService.createOrder(userDetails.getUser().getId(), request.getItems());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.<Order>builder()
                        .success(true)
                        .message("Đặt hàng thành công")
                        .status(HttpStatus.CREATED)
                        .data(order)
                        .build()
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<List<Order>>> myOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Order> orders = orderService.getOrdersByUser(userDetails.getUser().getId());
        return ResponseEntity.ok(
                APIResponse.<List<Order>>builder()
                        .success(true)
                        .message("Lịch sử đơn hàng")
                        .status(HttpStatus.OK)
                        .data(orders)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<APIResponse<List<Order>>> allOrders() {
        return ResponseEntity.ok(
                APIResponse.<List<Order>>builder()
                        .success(true)
                        .message("Tất cả đơn hàng")
                        .status(HttpStatus.OK)
                        .data(orderService.getAllOrders())
                        .build()
        );
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<APIResponse<String>> updateStatus(@PathVariable Long id,
                                                            @RequestParam OrderStatus status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok(
                APIResponse.<String>builder()
                        .success(true)
                        .message("Cập nhật trạng thái thành công")
                        .status(HttpStatus.OK)
                        .data(null)
                        .build()
        );
    }
}
