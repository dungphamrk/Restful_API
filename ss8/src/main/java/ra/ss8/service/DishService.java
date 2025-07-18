package ra.ss8.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.ss8.model.Dish;
import ra.ss8.model.dto.ApiResponse;
import ra.ss8.model.dto.DishDTO;
import ra.ss8.repository.DishRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private Cloudinary cloudinary;

    public ApiResponse<Dish> createDish(DishDTO dishDTO) {
        try {
            String imageUrl = uploadImageToCloudinary(dishDTO.getImage());
            Dish dish = new Dish(
                    dishDTO.getName(),
                    dishDTO.getDescription(),
                    dishDTO.getPrice(),
                    dishDTO.getStatus(),
                    imageUrl
            );
            Dish savedDish = dishRepository.save(dish);
            return new ApiResponse<>(true, "Dish created successfully", savedDish);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create dish: " + e.getMessage());
        }
    }

    public ApiResponse<Dish> updateDish(Long id, DishDTO dishDTO) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dish not found with id: " + id));

        dish.setName(dishDTO.getName());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setStatus(dishDTO.getStatus());

        if (dishDTO.getImage() != null && !dishDTO.getImage().isEmpty()) {
            String imageUrl = uploadImageToCloudinary(dishDTO.getImage());
            dish.setImage(imageUrl);
        }

        Dish updatedDish = dishRepository.save(dish);
        return new ApiResponse<>(true, "Dish updated successfully", updatedDish);
    }

    public ApiResponse<String> deleteDish(Long id) {
        if (!dishRepository.existsById(id)) {
            throw new NoSuchElementException("Dish not found with id: " + id);
        }
        dishRepository.deleteById(id);
        return new ApiResponse<>(true, "Dish deleted successfully", null);
    }

    public ApiResponse<List<Dish>> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return new ApiResponse<>(true, "Dishes retrieved successfully", dishes);
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