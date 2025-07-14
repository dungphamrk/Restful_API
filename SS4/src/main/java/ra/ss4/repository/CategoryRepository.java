package ra.ss4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss4.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}