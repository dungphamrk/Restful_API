package com.data.ss13.service.impl;

import com.data.ss13.model.entity.*;
import com.data.ss13.model.dto.request.CheckoutRequest;
import com.data.ss13.repository.OrderDetailRepository;
import com.data.ss13.repository.OrderRepository;
import com.data.ss13.repository.ProductCartRepository;
import com.data.ss13.service.OrderService;
import com.data.ss13.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ProductCartRepository cartRepo;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    @Override
    public void checkout(CheckoutRequest request) {
        User user = userService.getCurrentUser();
        List<ProductCart> cartItems = cartRepo.findAllByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();

        Order order = Order.builder()
                .receiver(request.getReceiver())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalMoney(total)
                .user(user)
                .build();
        orderRepository.save(order);

        for (ProductCart cartItem : cartItems) {
            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .priceBuy(cartItem.getProduct().getPrice())
                    .build();
            orderDetailRepository.save(detail);
        }

        cartRepo.deleteAll(cartItems);
    }
}