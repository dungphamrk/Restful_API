package ra.ss15.repository;

import ra.ss15.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long>{
    List<Order> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o JOIN o.orderDetails d " +
            "WHERE o.user.id = :userId AND d.product.id = :productId AND o.status = 'COMPLETED'")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT COALESCE(SUM(o.totalMoney), 0) FROM Order o WHERE DATE(o.createdDate) = :date AND o.status = 'COMPLETED'")
    BigDecimal sumTotalMoneyByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(o.totalMoney), 0) FROM Order o WHERE YEAR(o.createdDate) = :year AND MONTH(o.createdDate) = :month AND o.status = 'COMPLETED'")
    BigDecimal sumTotalMoneyByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(o.totalMoney), 0) FROM Order o WHERE YEAR(o.createdDate) = :year AND o.status = 'COMPLETED'")
    BigDecimal sumTotalMoneyByYear(@Param("year") int year);
}