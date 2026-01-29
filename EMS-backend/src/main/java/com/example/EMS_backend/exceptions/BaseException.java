package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom exceptions in the EMS application.
 * Provides common fields for error handling and HTTP status mapping.
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    
    /**
     * Constructor with message, error code, and HTTP status.
     *
     * @param message Human-readable error message
     * @param errorCode Application-specific error code
     * @param httpStatus HTTP status code for this exception
     */
    protected BaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    /**
     * Constructor with message, cause, error code, and HTTP status.
     *
     * @param message Human-readable error message
     * @param cause The underlying cause of this exception
     * @param errorCode Application-specific error code
     * @param httpStatus HTTP status code for this exception
     */
    protected BaseException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
