package ra.ss8.adviceController;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ra.ss8.model.dto.ApiResponse;

import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;




@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi khi đối số không hợp lệ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Invalid input",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.toList())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi vi phạm ràng buộc
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                false,
                "Constraint violation",
                ex.getConstraintViolations().stream()
                        .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                        .collect(Collectors.toList())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi không tìm thấy phần tử
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> handleNoSuchElementException(NoSuchElementException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Resource not found", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Xử lý lỗi phân tích ngày giờ
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse<String>> handleDateTimeParseException(DateTimeParseException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Invalid date format", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi không tìm thấy tài nguyên
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Resource not found", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Xử lý lỗi yêu cầu không hợp lệ
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Bad request", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Xử lý các ngoại lệ chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(false, "Internal server error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}