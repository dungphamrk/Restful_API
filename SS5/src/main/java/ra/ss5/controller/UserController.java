package ra.ss5.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.ss5.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý người dùng")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Lấy danh sách tất cả người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @GetMapping
    public ResponseEntity<DataResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(new DataResponse<>(users, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Lấy người dùng theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy người dùng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<User>> getUserById(
            @Parameter(description = "ID của người dùng", required = true)
            @PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(new DataResponse<>(user, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Tạo người dùng mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo người dùng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Username hoặc email đã tồn tại")
    })
    @PostMapping
    public ResponseEntity<DataResponse<User>> createUser(
            @Parameter(description = "Thông tin người dùng mới", required = true)
            @Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(new DataResponse<>(createdUser, HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Cập nhật thông tin người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @ApiResponse(responseCode = "409", description = "Username hoặc email đã tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<User>> updateUser(
            @Parameter(description = "ID của người dùng", required = true)
            @PathVariable Long id,
            @Parameter(description = "Thông tin người dùng cập nhật", required = true)
            @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return new ResponseEntity<>(new DataResponse<>(updatedUser, HttpStatus.OK), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Xóa người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteUser(
            @Parameter(description = "ID của người dùng", required = true)
            @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(new DataResponse<>("Xóa người dùng thành công", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Tìm người dùng theo username")
    @GetMapping("/username/{username}")
    public ResponseEntity<DataResponse<User>> getUserByUsername(
            @Parameter(description = "Username của người dùng", required = true)
            @PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return new ResponseEntity<>(new DataResponse<>(user, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Tìm người dùng theo email")
    @GetMapping("/email/{email}")
    public ResponseEntity<DataResponse<User>> getUserByEmail(
            @Parameter(description = "Email của người dùng", required = true)
            @PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<>(new DataResponse<>(user, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

