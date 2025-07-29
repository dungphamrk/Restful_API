package ra.ss15.service;

import ra.ss15.model.dto.request.LoginRequest;
import ra.ss15.model.dto.request.RegisterRequest;
import ra.ss15.model.dto.response.JWTResponse;
import ra.ss15.model.entity.Role;
import ra.ss15.model.entity.User;
import ra.ss15.repository.UserRepo;
import ra.ss15.secutiry.jwt.JWTProvider;
import ra.ss15.secutiry.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JWTProvider jwtUtil;
    private final AuthenticationManager authManager;

    public void register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email đã tồn tại");

        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .role(Role.valueOf("ROLE_USER"))
                .build();
        userRepo.save(user);
    }

    public JWTResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(String.valueOf(user));
        return new JWTResponse(token, "dummy-refresh-token");
    }
}
