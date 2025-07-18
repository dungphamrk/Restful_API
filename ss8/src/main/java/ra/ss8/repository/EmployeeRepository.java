package ra.ss8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ra.ss8.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
