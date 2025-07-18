package ra.ss8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}