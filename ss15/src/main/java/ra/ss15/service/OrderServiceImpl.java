package ra.ss15.service;

import ra.ss15.model.dto.request.OrderItemRequest;
import com.data.ss15.model.entity.*;
import ra.ss15.model.entity.*;
import ra.ss15.repository.OrderRepo;
import ra.ss15.repository.ProductRepo;
import ra.ss15.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    @Override
    public Order createOrder(Long userId, List<OrderItemRequest> itemsReq) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .createdDate(LocalDateTime.now())
                .build();

        for (OrderItemRequest req : itemsReq) {
            Product product = productRepo.findById(req.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity()));
            total = total.add(itemTotal);

            items.add(OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(req.getQuantity())
                    .priceBuy(product.getPrice())
                    .build());
        }

        order.setItems(items);
        order.setTotalMoney(total);

        return orderRepo.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn không tồn tại"));
        order.setStatus(status);
        orderRepo.save(order);
    }
}