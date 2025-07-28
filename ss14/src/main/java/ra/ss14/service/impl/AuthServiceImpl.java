package ra.ss14.service.impl;

import ra.ss14.model.dto.request.UserLogin;
import ra.ss14.model.dto.response.JWTResponse;
import ra.ss14.security.jwt.JWTProvider;
import ra.ss14.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ra.ss14.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Override
    public JWTResponse login(UserLogin userLogin) {
        if (userLogin.getUsername() == null || userLogin.getPassword() == null) {
            throw new IllegalArgumentException("Username or password cannot be null");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getUsername(),
                            userLogin.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtProvider.generateToken(userDetails.getUsername());
            log.info("Login successful for user: {}", userDetails.getUsername());

            return JWTResponse.builder()
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .status(true)
                    .token(accessToken)
                    .refreshToken(jwtProvider.generateToken(userDetails.getUsername()))
                    .authorities(userDetails.getAuthorities())
                    .build();
        } catch (AuthenticationException ex) {
            log.error("Login failed for user: {}", userLogin.getUsername(), ex);
            throw new RuntimeException("Invalid username or password");
        }
    }

}