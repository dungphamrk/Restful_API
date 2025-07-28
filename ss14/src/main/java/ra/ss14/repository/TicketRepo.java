package ra.ss14.repository;

import ra.ss14.model.entity.Ticket;
import ra.ss14.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Long>{
    List<Ticket> findByUser(User user);
}