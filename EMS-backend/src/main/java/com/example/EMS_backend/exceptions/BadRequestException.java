package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request contains invalid data.
 * Maps to HTTP 400 Bad Request.
 */
public class BadRequestException extends BaseException {
    
    private static final String ERROR_CODE = "BAD_REQUEST";
    
    /**
     * Constructor with custom message.
     *
     * @param message Error message describing what is invalid
     */
    public BadRequestException(String message) {
        super(message, ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Constructor with message and cause.
     *
     * @param message Error message
     * @param cause The underlying cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
