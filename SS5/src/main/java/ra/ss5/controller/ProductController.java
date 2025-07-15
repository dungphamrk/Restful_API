package ra.ss5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ra.ss5.entity.Product;
import ra.ss5.service.ProductService;

@Controller
@RequestMapping("/bt4")
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            return "bt4/products";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách sản phẩm");
            return "bt4/products";
        }
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm");
                return "redirect:/bt4";
            }
            model.addAttribute("product", product);
            return "bt4/product-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải thông tin sản phẩm");
            return "redirect:/bt4";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("action", "add");
        return "bt4/product-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Product product,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");
            return "bt4/product-form";
        }

        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            return "redirect:/bt4";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("action", "add");
            return "bt4/product-form";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm");
            model.addAttribute("action", "add");
            return "bt4/product-form";
        }
    }

    // UPDATE - Hiển thị form cập nhật sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm");
                return "redirect:/bt4";
            }
            model.addAttribute("product", product);
            model.addAttribute("action", "edit");
            return "bt4/product-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải thông tin sản phẩm");
            return "redirect:/bt4";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Product product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "bt4/product-form";
        }

        try {
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
            return "redirect:/bt4";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("action", "edit");
            return "bt4/product-form";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật sản phẩm");
            model.addAttribute("action", "edit");
            return "bt4/product-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm");
        }
        return "redirect:/bt4";
    }
}
