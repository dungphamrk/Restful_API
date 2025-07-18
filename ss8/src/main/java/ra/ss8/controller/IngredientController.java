package ra.ss8.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss8.model.Ingredient;
import ra.ss8.model.dto.IngredientDTO;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<ApiResponse<Ingredient>> createIngredient(@Valid @ModelAttribute IngredientDTO ingredientDTO) {
        ApiResponse<Ingredient> response = ingredientService.createIngredient(ingredientDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ingredient>> updateIngredient(@PathVariable Long id, @Valid @ModelAttribute IngredientDTO ingredientDTO) {
        ApiResponse<Ingredient> response = ingredientService.updateIngredient(id, ingredientDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteIngredient(@PathVariable Long id) {
        ApiResponse<String> response = ingredientService.deleteIngredient(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ingredient>>> getAllIngredients() {
        ApiResponse<List<Ingredient>> response = ingredientService.getAllIngredients();
        return ResponseEntity.ok(response);
    }
}