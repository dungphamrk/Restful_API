package ra.ss7.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss7.model.Harvest;

public interface HarvestRepository extends JpaRepository<Harvest,Long> {
}
