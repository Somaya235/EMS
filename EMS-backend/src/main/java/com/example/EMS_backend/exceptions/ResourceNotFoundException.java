package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends BaseException {
    
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";
    
    /**
     * Constructor with resource type and ID.
     *
     * @param resourceType Type of resource (e.g., "Event", "User")
     * @param resourceId ID of the resource that was not found
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(
            String.format("%s with ID '%s' not found", resourceType, resourceId),
            ERROR_CODE,
            HttpStatus.NOT_FOUND
        );
    }
    
    /**
     * Constructor with custom message.
     *
     * @param message Custom error message
     */
    public ResourceNotFoundException(String message) {
        super(message, ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}
