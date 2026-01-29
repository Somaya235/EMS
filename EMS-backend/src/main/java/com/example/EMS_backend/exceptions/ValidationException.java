package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

/**
 * Exception thrown when validation fails.
 * Maps to HTTP 422 Unprocessable Entity.
 * Supports field-level validation errors.
 */
public class ValidationException extends BaseException {
    
    private static final String ERROR_CODE = "VALIDATION_ERROR";
    private final Map<String, String> fieldErrors;
    
    /**
     * Constructor with custom message.
     *
     * @param message Error message
     */
    public ValidationException(String message) {
        super(message, ERROR_CODE, HttpStatus.UNPROCESSABLE_ENTITY);
        this.fieldErrors = new HashMap<>();
    }
    
    /**
     * Constructor with field-level errors.
     *
     * @param message Error message
     * @param fieldErrors Map of field names to error messages
     */
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message, ERROR_CODE, HttpStatus.UNPROCESSABLE_ENTITY);
        this.fieldErrors = fieldErrors != null ? fieldErrors : new HashMap<>();
    }
    
    /**
     * Constructor for single field error.
     *
     * @param fieldName Name of the invalid field
     * @param errorMessage Error message for the field
     */
    public ValidationException(String fieldName, String errorMessage) {
        super("Validation failed", ERROR_CODE, HttpStatus.UNPROCESSABLE_ENTITY);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(fieldName, errorMessage);
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
