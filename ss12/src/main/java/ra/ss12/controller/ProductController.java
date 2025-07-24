package ra.ss12.controller;

import ra.ss12.model.dto.request.ApiResponse;
import ra.ss12.model.dto.request.ProductRequestDTO;
import ra.ss12.model.dto.response.ProductResponseDTO;
import ra.ss12.model.entity.Product;
import ra.ss12.model.mapper.ProductMapper;
import ra.ss12.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController{

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAll() {
        List<ProductResponseDTO> products = productService.findAll().stream()
                .map(ProductMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(products, "Lấy danh sách thành công", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> create(@Valid @RequestBody ProductRequestDTO dto) {
        Product product = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(ProductMapper.toResponseDTO(product), "Thêm sản phẩm thành công", 201));
    }

}