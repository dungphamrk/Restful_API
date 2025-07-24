package ra.ss12.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ra.ss12.model.dto.request.ProductRequestDTO;
import ra.ss12.model.entity.Product;
import ra.ss12.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.ss12.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product create(ProductRequestDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        return productRepo.save(product);
    }

    @Override
    public Product update(Long id, ProductRequestDTO dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        modelMapper.map(dto, product);
        return productRepo.save(product);
    }

    @Override
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepo.deleteById(id);
    }
}
