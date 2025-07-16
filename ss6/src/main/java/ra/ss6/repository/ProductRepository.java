package ra.ss6.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss6.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
   Page<Product> findByNameContainingIgnoreCase(String searchName, Pageable pageable);
}
