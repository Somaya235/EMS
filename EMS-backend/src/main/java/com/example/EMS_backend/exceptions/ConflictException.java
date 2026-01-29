package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource conflict occurs.
 * Maps to HTTP 409 Conflict.
 * Common use cases: duplicate email, duplicate resource, concurrent modification.
 */
public class ConflictException extends BaseException {
    
    private static final String ERROR_CODE = "CONFLICT";
    
    /**
     * Constructor with custom message.
     *
     * @param message Error message describing the conflict
     */
    public ConflictException(String message) {
        super(message, ERROR_CODE, HttpStatus.CONFLICT);
    }
    
    /**
     * Constructor for resource conflicts.
     *
     * @param resourceType Type of resource
     * @param conflictField Field that has the conflict
     * @param value Value that conflicts
     */
    public ConflictException(String resourceType, String conflictField, Object value) {
        super(
            String.format("%s with %s '%s' already exists", resourceType, conflictField, value),
            ERROR_CODE,
            HttpStatus.CONFLICT
        );
    }
}
