package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a password reset token is invalid or expired.
 * Maps to HTTP 400 Bad Request.
 */
public class TokenInvalidException extends BaseException {
    
    private static final String ERROR_CODE = "TOKEN_INVALID";
    
    /**
     * Constructor with custom message.
     *
     * @param message Custom error message
     */
    public TokenInvalidException(String message) {
        super(message, ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
