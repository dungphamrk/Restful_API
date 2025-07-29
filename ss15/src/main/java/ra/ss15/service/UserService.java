package ra.ss15.service;

import ra.ss15.model.entity.User;

public interface UserService {
    void updateUserRole(Long userId, String newRole);
    User getCurrentUser();
}