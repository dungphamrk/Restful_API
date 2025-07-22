package ra.ss10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss10.entity.model.CreditCard;
import ra.ss10.entity.model.TransactionCredit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionCreditRepository extends JpaRepository<TransactionCredit, UUID> {
    List<TransactionCredit> findByCreditCardSenderAndCreatedAtBetween(
            CreditCard creditCard, LocalDateTime from, LocalDateTime to);
}
