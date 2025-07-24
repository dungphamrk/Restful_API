package ra.ss12.service.impl;

import ra.ss12.model.entity.User;
import ra.ss12.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.ss12.service.RoleService;
import ra.ss12.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String username, String password) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(true);
        user.setRoles(roleService.getDefaultRoles());

        return userRepo.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}