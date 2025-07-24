package ra.ss12.repository;
import ra.ss12.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long>{
}
