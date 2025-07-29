package ra.ss15.service;

import ra.ss15.model.entity.Product;
import ra.ss15.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product create(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product update(Long id, Product updated) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setSize(updated.getSize());
        existing.setToppings(updated.getToppings());
        return productRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);
    }
}