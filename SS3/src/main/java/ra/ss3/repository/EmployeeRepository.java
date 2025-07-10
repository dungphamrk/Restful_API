package ra.ss3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.ss3.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.salary > :salary")
    List<Employee> findBySalaryGreaterThan(@Param("salary") double salary);
}
