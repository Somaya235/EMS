package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails.
 * Maps to HTTP 401 Unauthorized.
 */
public class UnauthorizedException extends BaseException {
    
    private static final String ERROR_CODE = "UNAUTHORIZED";
    
    /**
     * Constructor with custom message.
     *
     * @param message Error message describing the authentication failure
     */
    public UnauthorizedException(String message) {
        super(message, ERROR_CODE, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Default constructor with standard message.
     */
    public UnauthorizedException() {
        super("Authentication required", ERROR_CODE, HttpStatus.UNAUTHORIZED);
    }
}
