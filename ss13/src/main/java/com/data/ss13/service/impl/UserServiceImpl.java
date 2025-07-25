package com.data.ss13.service.impl;

import com.data.ss13.model.entity.UserRole;
import com.data.ss13.model.entity.Role;
import com.data.ss13.model.entity.User;
import com.data.ss13.model.dto.request.UserLogin;
import com.data.ss13.model.dto.request.UserRegister;
import com.data.ss13.model.dto.response.JWTResponse;
import com.data.ss13.repository.RoleRepository;
import com.data.ss13.repository.UserRepository;
import com.data.ss13.security.jwt.JWTProvider;
import com.data.ss13.security.principal.CustomUserDetails;
import com.data.ss13.service.UserService;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTProvider jwtProvider;
    private AuthenticationManager authenticationManager;

    @Override
    public User editUser(User user){
        if(userRepository.existsById(user.getId())){
            return userRepository.save(user);
        }
        throw new EntityNotFoundException("Khong ton tai id");
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Người dùng chưa đăng nhập");
        }

        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new SecurityException("Không xác định được thông tin người dùng");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với username: " + username));
    }


    @Override
    public User registerUser(UserRegister userRegister) {
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = roleRepository.findByName(String.valueOf(UserRole.ROLE_USER))
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

        User user = User.builder()
                .username(userRegister.getUsername())
                .email(userRegister.getEmail())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .roles(Set.of(userRole))
                .build();

        return userRepository.save(user);
    }


    @Override
    public JWTResponse login(UserLogin userLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getUsername(),
                            userLogin.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userDetails.getUsername());

            return JWTResponse.builder()
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .status(userDetails.isEnabled())
                    .authorities(userDetails.getAuthorities())
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            log.error("Sai username hoặc password: {}", e.getMessage());
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không chính xác.");
        }
    }

}