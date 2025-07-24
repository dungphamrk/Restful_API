package ra.ss12.service;

import ra.ss12.model.entity.User;

public interface UserService {
    User register(String username, String password);
    User login(String username, String password);
    User findByUsername(String username);
}