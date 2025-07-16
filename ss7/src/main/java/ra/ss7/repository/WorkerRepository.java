package ra.ss7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.ss7.model.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}