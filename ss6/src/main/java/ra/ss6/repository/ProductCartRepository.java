package ra.ss6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss6.model.ProductCart;
import ra.ss6.model.User;

import java.util.List;

@Repository
public interface ProductCartRepository extends JpaRepository<ProductCart, Long> {
    List<ProductCart> findAllByUser(User user);
}
