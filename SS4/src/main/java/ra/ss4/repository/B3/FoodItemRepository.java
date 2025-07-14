package ra.ss4.repository.B3;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.ss4.entity.B3.Category;
import ra.ss4.entity.B3.FoodItem;
import ra.ss4.entity.B3.FoodStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    Page<FoodItem> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<FoodItem> findByCategory(Category category, Pageable pageable);

    Page<FoodItem> findByStatus(FoodStatus status, Pageable pageable);

    Page<FoodItem> findByNameContainingIgnoreCaseAndCategory(String name, Category category, Pageable pageable);

    Page<FoodItem> findByNameContainingIgnoreCaseAndStatus(String name, FoodStatus status, Pageable pageable);

    Page<FoodItem> findByCategoryAndStatus(Category category, FoodStatus status, Pageable pageable);

    Page<FoodItem> findByNameContainingIgnoreCaseAndCategoryAndStatus(
            String name, Category category, FoodStatus status, Pageable pageable);

    List<FoodItem> findByExpirationDateBefore(LocalDate date);

    List<FoodItem> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT f FROM FoodItem f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR f.category.id = :categoryId) AND " +
            "(:status IS NULL OR f.status = :status)")
    Page<FoodItem> findWithFilters(@Param("name") String name,
                                   @Param("categoryId") Long categoryId,
                                   @Param("status") FoodStatus status,
                                   Pageable pageable);
}
