package ra.ss6.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.ss6.model.Order;
import ra.ss6.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByDate(LocalDate date) {
        return orderRepository.findByOrderDate(date);
    }
}
