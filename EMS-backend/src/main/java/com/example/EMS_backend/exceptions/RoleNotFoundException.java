package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a required role is missing.
 * Maps to HTTP 404 Not Found.
 */
public class RoleNotFoundException extends BaseException {

    private static final String ERROR_CODE = "ROLE_NOT_FOUND";

    public RoleNotFoundException(String roleName) {
        super(String.format("Role '%s' not found", roleName),
                ERROR_CODE,
                HttpStatus.NOT_FOUND);
    }
}

