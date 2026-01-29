package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is authenticated but lacks permission.
 * Maps to HTTP 403 Forbidden.
 */
public class ForbiddenException extends BaseException {
    
    private static final String ERROR_CODE = "FORBIDDEN";
    
    /**
     * Constructor with custom message.
     *
     * @param message Error message describing the authorization failure
     */
    public ForbiddenException(String message) {
        super(message, ERROR_CODE, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Default constructor with standard message.
     */
    public ForbiddenException() {
        super("Access denied", ERROR_CODE, HttpStatus.FORBIDDEN);
    }
}
