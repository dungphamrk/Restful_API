package ra.ss12.controller;

import ra.ss12.model.dto.request.ApiResponse;
import ra.ss12.model.dto.request.UserRequestDTO;
import ra.ss12.model.dto.response.UserResponseDTO;
import ra.ss12.model.entity.User;
import ra.ss12.model.mapper.UserMapper;
import ra.ss12.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody UserRequestDTO dto) {
        User user = userService.register(dto.getUsername(), dto.getPassword());
        UserResponseDTO responseDTO = UserMapper.toResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(responseDTO, "Đăng ký thành công", 201));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@Valid @RequestBody UserRequestDTO dto, HttpSession session) {
        User user = userService.login(dto.getUsername(), dto.getPassword());
        session.setAttribute("username", user.getUsername());
        UserResponseDTO responseDTO = UserMapper.toResponseDTO(user);
        return ResponseEntity.ok(new ApiResponse<>(responseDTO, "Đăng nhập thành công", 200));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiResponse<>(null, "Đăng xuất thành công", 200));
    }
}