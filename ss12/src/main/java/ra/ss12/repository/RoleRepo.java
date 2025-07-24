package ra.ss12.repository;

import ra.ss12.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long>{
    Optional<Role> findByRoleName(String name);
}
