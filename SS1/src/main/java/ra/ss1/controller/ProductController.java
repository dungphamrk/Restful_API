package ra.ss1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.ss1.entity.Product;
import ra.ss1.repository.ProductRepository;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 👉 Trang danh sách sản phẩm (Thymeleaf)
    @GetMapping("/view")
    public String viewProductPage(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        return "product-list"; // Tương ứng file product-list.html
    }

    // 👉 Thêm sản phẩm từ form
    @PostMapping
    public String saveProductFromForm(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products/view";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return "redirect:/products/view";
        }
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product) {
        productRepository.update(product);
        return "redirect:/products/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductFromPage(@PathVariable Long id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            productRepository.delete(id);
        }
        return "redirect:/products/view";
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductAPI(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existing = productRepository.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        productRepository.update(existing);
        return ResponseEntity.ok("Cập nhật sản phẩm thành công!");
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductAPI(@PathVariable Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công!");
    }
}
