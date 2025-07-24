package ra.ss12.service;

import ra.ss12.model.entity.Role;
import java.util.Set;

public interface RoleService {
    Role findByName(String roleName);
    Set<Role> getDefaultRoles();
}
