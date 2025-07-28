package ra.ss14.service.impl;

import ra.ss14.model.dto.request.UserRegister;
import ra.ss14.model.dto.response.UserResponseDTO;
import ra.ss14.model.entity.Role;
import ra.ss14.model.entity.User;
import ra.ss14.model.enums.ERole;
import ra.ss14.repository.RoleRepo;
import ra.ss14.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.ss14.service.UserService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegister userRegister) {
        if (isUsernameTaken(userRegister.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (isEmailTaken(userRegister.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        Role userRole = roleRepo.findByName(ERole.ROLE_USER.name())
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .email(userRegister.getEmail())
                .roles(Set.of(userRole))
                .build();

        userRepo.save(user);

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepo.existsByEmail(email);
    }
}