package ra.ss9.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        logger.atError()
                .addKeyValue("errorType", "unhandled_exception")
                .addKeyValue("errorClass", ex.getClass().getSimpleName())
                .addKeyValue("errorMessage", ex.getMessage())
                .addKeyValue("timestamp", LocalDateTime.now().toString())
                .log("Unhandled exception occurred", ex);

        Map<String, Object> errorResponse = Map.of(
                "error", "Internal Server Error",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
