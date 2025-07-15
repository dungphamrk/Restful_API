package ra.ss5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss5.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
