package ra.ss6.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss6.model.DataResponse;
import ra.ss6.model.ProductCart;
import ra.ss6.service.ProductCartService;
import ra.ss6.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/product-cart")
public class ProductCartController {

    @Autowired
    private ProductCartService productCartService;

    @Autowired
    private UserService userService;

    @GetMapping("/cart")
    public ResponseEntity<DataResponse<List<ProductCart>>> getCartByUser(@RequestParam Long userId) {
        var user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new DataResponse<>(null, org.springframework.http.HttpStatus.BAD_REQUEST));
        }

        List<ProductCart> cartItems = productCartService.getCartItemsByUser(user);
        return ResponseEntity.ok(new DataResponse<>(cartItems, org.springframework.http.HttpStatus.OK));
    }

    @PostMapping("/cart")
    public ResponseEntity<DataResponse<ProductCart>> addToCart(@RequestBody ProductCart productCart) {
        ProductCart savedCart = productCartService.addToCart(productCart);
        return ResponseEntity.ok(new DataResponse<>(savedCart, org.springframework.http.HttpStatus.CREATED));
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<DataResponse<ProductCart>> updateCartQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        ProductCart updatedCart = productCartService.updateQuantity(id, quantity);
        if (updatedCart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DataResponse<>(updatedCart, org.springframework.http.HttpStatus.OK));
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<DataResponse<String>> removeFromCart(@PathVariable Long id) {
        productCartService.removeFromCart(id);
        return ResponseEntity.ok(new DataResponse<>("Đã xóa sản phẩm khỏi giỏ hàng", org.springframework.http.HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<ProductCart>>> getAllProductCarts() {
        List<ProductCart> productCarts = productCartService.getAllProductCarts();
        return ResponseEntity.ok(new DataResponse<>(productCarts, org.springframework.http.HttpStatus.OK));
    }
}
