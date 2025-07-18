package ra.ss8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}