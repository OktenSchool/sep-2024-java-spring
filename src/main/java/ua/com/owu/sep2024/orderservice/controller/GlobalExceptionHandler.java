package ua.com.owu.sep2024.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.owu.sep2024.orderservice.dto.ErrorDto;
import ua.com.owu.sep2024.orderservice.exception.ShopIsNotAccessibleException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> details = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            details.put(field, message);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .message("Validation failed")
                        .timestamp(Instant.now())
                        .details(details).build());
    }

    @ExceptionHandler(ShopIsNotAccessibleException.class)
    public ResponseEntity<ErrorDto> handleShopIsNotAccessibleException(ShopIsNotAccessibleException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorDto.builder()
                        .message(e.getMessage())
                        .timestamp(Instant.now())
                        .details(Map.of()).build());
    }
}
