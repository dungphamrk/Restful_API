package ra.ss15.service;

import ra.ss15.model.dto.request.OrderItemRequest;
import ra.ss15.model.entity.Order;
import ra.ss15.model.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, List<OrderItemRequest> items);
    List<Order> getOrdersByUser(Long userId);
    List<Order> getAllOrders();
    void updateStatus(Long orderId, OrderStatus status);
}
