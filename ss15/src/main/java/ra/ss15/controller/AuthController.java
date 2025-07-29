package ra.ss15.controller;

import ra.ss15.model.dto.request.LoginRequest;
import ra.ss15.model.dto.request.RegisterRequest;
import ra.ss15.model.dto.response.APIResponse;
import ra.ss15.model.dto.response.JWTResponse;
import ra.ss15.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.<String>builder()
                        .success(true)
                        .message("Đăng ký thành công")
                        .status(HttpStatus.CREATED)
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@RequestBody LoginRequest req) {
        JWTResponse jwtResponse = authService.login(req);
        return ResponseEntity.ok(
                APIResponse.<JWTResponse>builder()
                        .success(true)
                        .message("Đăng nhập thành công")
                        .status(HttpStatus.OK)
                        .data(jwtResponse)
                        .build()
        );
    }
}
