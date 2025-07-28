package ra.ss14.exception;

import ra.ss14.model.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new APIResponse<>(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new APIResponse<>(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse<>(false, ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
