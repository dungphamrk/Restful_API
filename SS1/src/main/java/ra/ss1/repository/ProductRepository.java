package ra.ss1.repository;

import ra.ss1.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(Long id);
    void save(Product product);
    void update(Product product);
    void delete(Long id);
}
