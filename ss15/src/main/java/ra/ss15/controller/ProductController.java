package ra.ss15.controller;

import ra.ss15.model.dto.response.APIResponse;
import ra.ss15.model.entity.Product;
import ra.ss15.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_USER')")
    public ResponseEntity<APIResponse<List<Product>>> getAll() {
        return ResponseEntity.ok(
                APIResponse.<List<Product>>builder()
                        .success(true)
                        .message("Danh sách sản phẩm")
                        .status(HttpStatus.OK)
                        .data(productService.findAll())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<APIResponse<Product>> create(@RequestBody @Valid Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.<Product>builder()
                        .success(true)
                        .message("Tạo sản phẩm thành công")
                        .status(HttpStatus.CREATED)
                        .data(productService.create(product))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<APIResponse<Product>> update(@PathVariable Long id, @RequestBody @Valid Product product) {
        return ResponseEntity.ok(
                APIResponse.<Product>builder()
                        .success(true)
                        .message("Cập nhật thành công")
                        .status(HttpStatus.OK)
                        .data(productService.update(id, product))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(
                APIResponse.<String>builder()
                        .success(true)
                        .message("Xóa thành công")
                        .status(HttpStatus.NO_CONTENT)
                        .data(null)
                        .build()
        );
    }
}