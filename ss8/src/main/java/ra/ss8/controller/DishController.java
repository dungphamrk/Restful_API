package ra.ss8.controller;




import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss8.model.Dish;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.model.dto.DishDTO;
import ra.ss8.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public ResponseEntity<ApiResponse<Dish>> createDish(@Valid @ModelAttribute DishDTO dishDTO) {
        ApiResponse<Dish> response = dishService.createDish(dishDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Dish>> updateDish(@PathVariable Long id, @Valid @ModelAttribute DishDTO dishDTO) {
        ApiResponse<Dish> response = dishService.updateDish(id, dishDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDish(@PathVariable Long id) {
        ApiResponse<String> response = dishService.deleteDish(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Dish>>> getAllDishes() {
        ApiResponse<List<Dish>> response = dishService.getAllDishes();
        return ResponseEntity.ok(response);
    }
}