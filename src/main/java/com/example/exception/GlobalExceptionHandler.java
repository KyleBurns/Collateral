package com.example.collateral;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CollateralServiceException.class)
    public ResponseEntity<Map<String, String>> handleCollateralServiceException(CollateralServiceException ex) {
        Map<String, String> body = Map.of(
            "error", "Collateral Service Error",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> body = Map.of(
            "error", "Internal Server Error",
            "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}