package ra.ss8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}