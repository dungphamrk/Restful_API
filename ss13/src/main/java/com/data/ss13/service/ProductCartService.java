package com.data.ss13.service;

import com.data.ss13.model.entity.ProductCart;
import com.data.ss13.model.dto.request.CartRequest;

import java.util.List;

public interface ProductCartService {
    void addToCart(CartRequest request);
    void removeFromCart(Long productId);
    void updateQuantity(Long productId, Integer quantity);
    List<ProductCart> getUserCart();
}