package ra.ss14.repository;

import ra.ss14.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);
}