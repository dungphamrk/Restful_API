package ra.ss7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss7.model.PaymentSlip;

@Repository
public interface PaymentSlipRepository extends JpaRepository<PaymentSlip, Long> {
}