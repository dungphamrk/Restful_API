package ra.ss8.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.ss8.model.Ingredient;
import ra.ss8.model.dto.IngredientDTO;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.repository.IngredientRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private Cloudinary cloudinary;

    public ApiResponse<Ingredient> createIngredient(IngredientDTO ingredientDTO) {
        try {
            String imageUrl = uploadImageToCloudinary(ingredientDTO.getImage());
            Ingredient ingredient = new Ingredient(
                    ingredientDTO.getName(),
                    ingredientDTO.getStock(),
                    ingredientDTO.getExpiry(),
                    imageUrl
            );
            Ingredient savedIngredient = ingredientRepository.save(ingredient);
            return new ApiResponse<>(true, "Ingredient created successfully", savedIngredient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ingredient: " + e.getMessage());
        }
    }

    public ApiResponse<Ingredient> updateIngredient(Long id, IngredientDTO ingredientDTO) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ingredient not found with id: " + id));

        ingredient.setName(ingredientDTO.getName());
        ingredient.setStock(ingredientDTO.getStock());
        ingredient.setExpiry(ingredientDTO.getExpiry());

        if (ingredientDTO.getImage() != null && !ingredientDTO.getImage().isEmpty()) {
            String imageUrl = uploadImageToCloudinary(ingredientDTO.getImage());
            ingredient.setImage(imageUrl);
        }

        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return new ApiResponse<>(true, "Ingredient updated successfully", updatedIngredient);
    }

    public ApiResponse<String> deleteIngredient(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new NoSuchElementException("Ingredient not found with id: " + id);
        }
        ingredientRepository.deleteById(id);
        return new ApiResponse<>(true, "Ingredient deleted successfully", null);
    }

    public ApiResponse<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return new ApiResponse<>(true, "Ingredients retrieved successfully", ingredients);
    }

    private String uploadImageToCloudinary(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }
}