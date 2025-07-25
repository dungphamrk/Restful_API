package com.data.ss13.service.impl;

import com.data.ss13.model.entity.Product;
import com.data.ss13.repository.ProductRepository;
import com.data.ss13.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.warn("No products found in the database");
        }
        return products;
    }

    @Override
    public Product addProduct(Product product) {
        validateProduct(product);
        log.info("Adding new product: {}", product.getName());
        return productRepository.save(product);
    }

    @Override
    public Product editProduct(Product product) {
        validateProduct(product);
        log.info("Editing product with ID: {}", product.getId());
        return productRepository.findById(product.getId())
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", product.getId());
                    return new EntityNotFoundException("Product not found with ID: " + product.getId());
                });
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.error("Product with ID {} not found", id);
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!StringUtils.hasText(product.getName())) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
    }
}