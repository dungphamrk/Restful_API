package ra.ss4.service.B3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.ss4.entity.B3.Category;
import ra.ss4.entity.B3.FoodItem;
import ra.ss4.entity.B3.FoodStatus;
import ra.ss4.repository.B3.CategoryRepository;
import ra.ss4.repository.B3.FoodItemRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<FoodItem> findAll(Pageable pageable) {
        return foodItemRepository.findAll(pageable);
    }

    public Optional<FoodItem> findById(Long id) {
        return foodItemRepository.findById(id);
    }

    public FoodItem save(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    public void deleteById(Long id) {
        foodItemRepository.deleteById(id);
    }

    public void deleteByIds(Long[] ids) {
        foodItemRepository.deleteAllById(Arrays.asList(ids));
    }

    public Page<FoodItem> searchByName(String name, Pageable pageable) {
        return foodItemRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<FoodItem> filterByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return foodItemRepository.findByCategory(category, pageable);
    }

    public Page<FoodItem> filterByStatus(String status, Pageable pageable) {
        FoodStatus foodStatus = FoodStatus.valueOf(status.toUpperCase());
        return foodItemRepository.findByStatus(foodStatus, pageable);
    }

    public Page<FoodItem> searchAndFilter(String name, Long categoryId, String status, Pageable pageable) {
        FoodStatus foodStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                foodStatus = FoodStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore invalid status
            }
        }

        return foodItemRepository.findWithFilters(name, categoryId, foodStatus, pageable);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    // Tìm thực phẩm hết hạn
    public List<FoodItem> findExpiredItems() {
        return foodItemRepository.findByExpirationDateBefore(LocalDate.now());
    }

    // Tìm thực phẩm sắp hết hạn (trong 7 ngày tới)
    public List<FoodItem> findItemsExpiringSoon() {
        LocalDate now = LocalDate.now();
        LocalDate weekLater = now.plusDays(7);
        return foodItemRepository.findByExpirationDateBetween(now, weekLater);
    }

    // Thống kê số lượng theo trạng thái
    public long countByStatus(FoodStatus status) {
        return foodItemRepository.findByStatus(status, Pageable.unpaged()).getTotalElements();
    }
}
