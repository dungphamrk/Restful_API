package com.data.ss13.service;

import com.data.ss13.model.entity.Product;

import java.util.List;

public interface ProductService{
    List<Product> getAllProducts();
    Product addProduct(Product product);
    Product editProduct(Product product);
    void deleteProduct(Long id);
}
