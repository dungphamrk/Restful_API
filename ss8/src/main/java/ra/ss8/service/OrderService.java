package ra.ss8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.ss8.model.*;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.model.dto.OrderDTO;
import ra.ss8.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DishRepository dishRepository;

    @Transactional
    public ApiResponse<Order> createOrder(OrderDTO orderDTO) {
        try {
            // Validate customer
            Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + orderDTO.getCustomerId()));
            if (!customer.isStatus()) {
                throw new NoSuchElementException("Customer with id: " + orderDTO.getCustomerId() + " has been deleted");
            }

            // Validate employee
            Employee employee = employeeRepository.findById(orderDTO.getEmployeeId())
                    .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + orderDTO.getEmployeeId()));

            // Create order
            Order order = new Order();
            order.setCustomer(customer);
            order.setEmployee(employee);
            order.setCreatedAt(LocalDateTime.now());

            // Calculate total money and validate order details
            double totalMoney = 0.0;
            List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream().map(detailDTO -> {
                Dish dish = dishRepository.findById(detailDTO.getDishId())
                        .orElseThrow(() -> new NoSuchElementException("Dish not found with id: " + detailDTO.getDishId()));
                if (detailDTO.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Quantity must be greater than 0 for dish id: " + detailDTO.getDishId());
                }
                totalMoney += dish.getPrice() * detailDTO.getQuantity();
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setDish(dish);
                orderDetail.setQuantity(detailDTO.getQuantity());
                orderDetail.setPriceBuy(dish.getPrice());
                return orderDetail;
            }).collect(Collectors.toList());

            order.setTotalMoney(totalMoney);
            Order savedOrder = orderRepository.save(order);

            // Save order details
            for (OrderDetail detail : orderDetails) {
                detail.setOrder(savedOrder);
                orderDetailRepository.save(detail);
            }

            return new ApiResponse<>(true, "Order created successfully", savedOrder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    public ApiResponse<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return new ApiResponse<>(true, "Orders retrieved successfully", orders);
    }
}