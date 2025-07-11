package com.example.collateral;

/**
 * Exception thrown when a validation error occurs or an external service fails.
 */
public class CollateralServiceException extends RuntimeException {
    public CollateralServiceException(String message) {
        super(message);
    }

    public CollateralServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}