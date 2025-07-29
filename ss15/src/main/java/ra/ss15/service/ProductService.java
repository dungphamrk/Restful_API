package ra.ss15.service;

import ra.ss15.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product create(Product product);
    Product update(Long id, Product updated);
    void delete(Long id);
}
