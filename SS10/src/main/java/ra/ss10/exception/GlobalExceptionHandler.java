        package ra.ss10.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ra.ss10.entity.dto.response.DataErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataErrorResponse<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.error("Validation error occurred: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(new DataErrorResponse<>("Validation failed", HttpStatus.BAD_REQUEST, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<DataErrorResponse<String>> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        String message = ex.getMessage().contains("CCCD") ? "Tài khoản không tồn tại với CCCD đã cung cấp" :
                ex.getMessage().contains("ID") ? "Tài khoản không tồn tại với ID đã cung cấp" :
                        "Tài khoản không tồn tại";
        return new ResponseEntity<>(new DataErrorResponse<>("Tài khoản không tìm thấy", HttpStatus.NOT_FOUND, message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataErrorResponse<String>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new DataErrorResponse<>("Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi không mong muốn"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
