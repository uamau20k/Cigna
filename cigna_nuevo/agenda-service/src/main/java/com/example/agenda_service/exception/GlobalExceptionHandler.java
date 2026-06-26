package com.example.agenda_service.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        logger.warn("Not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now()).status(404).error("Not Found").message(ex.getMessage()).path(req.getRequestURI()).build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now()).status(400).error("Bad Request").message(ex.getMessage()).path(req.getRequestURI()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now()).status(400).error("Validation Error").message(msg).path(req.getRequestURI()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
        logger.error("Error interno: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.builder()
                .timestamp(OffsetDateTime.now()).status(500).error("Internal Server Error").message("Error interno").path(req.getRequestURI()).build());
    }
}
