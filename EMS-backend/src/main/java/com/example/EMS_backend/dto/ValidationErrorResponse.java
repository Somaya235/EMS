package com.example.EMS_backend.dto;

import java.util.Map;
import java.util.HashMap;

/**
 * Extended error response for validation errors.
 * Includes field-level error details.
 */
public class ValidationErrorResponse extends ErrorResponse {
    
    private Map<String, String> fieldErrors;
    
    public ValidationErrorResponse() {
        super();
        this.fieldErrors = new HashMap<>();
    }
    
    public ValidationErrorResponse(int status, String error, String message, String path, String errorCode, Map<String, String> fieldErrors) {
        super(status, error, message, path, errorCode);
        this.fieldErrors = fieldErrors != null ? fieldErrors : new HashMap<>();
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
    
    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
    
    public void addFieldError(String field, String message) {
        if (this.fieldErrors == null) {
            this.fieldErrors = new HashMap<>();
        }
        this.fieldErrors.put(field, message);
    }
}
