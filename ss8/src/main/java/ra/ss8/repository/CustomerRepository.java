package ra.ss8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByStatusTrue();
}