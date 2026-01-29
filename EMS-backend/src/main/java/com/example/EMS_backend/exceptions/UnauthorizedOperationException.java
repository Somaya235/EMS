package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an authenticated user attempts an operation
 * they are not authorized to perform.
 * Maps to HTTP 403 Forbidden.
 */
public class UnauthorizedOperationException extends BaseException {

    private static final String ERROR_CODE = "UNAUTHORIZED_OPERATION";

    public UnauthorizedOperationException(String message) {
        super(message, ERROR_CODE, HttpStatus.FORBIDDEN);
    }
}

