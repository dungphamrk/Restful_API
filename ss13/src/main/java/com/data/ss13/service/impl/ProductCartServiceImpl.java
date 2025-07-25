package com.data.ss13.service.impl;

import com.data.ss13.model.dto.request.ProductCartDTO;
import com.data.ss13.model.entity.Product;
import com.data.ss13.model.entity.ProductCart;
import com.data.ss13.model.entity.User;
import com.data.ss13.model.dto.request.CartRequest;
import com.data.ss13.repository.ProductCartRepository;
import com.data.ss13.repository.ProductRepository;
import com.data.ss13.service.ProductCartService;
import com.data.ss13.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCartServiceImpl implements ProductCartService {

    private final ProductCartRepository cartRepo;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public void addToCart(CartRequest request) {
        User user = userService.getCurrentUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCart cart = cartRepo.findByUserAndProduct(user, product).orElse(null);
        if (cart == null) {
            cart = ProductCart.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
        } else {
            cart.setQuantity(cart.getQuantity() + request.getQuantity());
        }
        cartRepo.save(cart);
    }

    @Override
    public void removeFromCart(Long productId) {
        User user = userService.getCurrentUser();
        cartRepo.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Override
    public void updateQuantity(Long productId, Integer quantity) {
        User user = userService.getCurrentUser();
        ProductCart cart = cartRepo.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setQuantity(quantity);
        cartRepo.save(cart);
    }

    @Override
    public List<ProductCart> getUserCart() {
        User user = userService.getCurrentUser();
        return new ArrayList<>(cartRepo.findAllByUserId(user.getId()));
    }
}