package ra.ss8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {
}