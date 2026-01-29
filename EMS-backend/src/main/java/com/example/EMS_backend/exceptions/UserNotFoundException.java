package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested user is not found.
 * Maps to HTTP 404 Not Found.
 */
public class UserNotFoundException extends BaseException {

    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserNotFoundException(Long userId) {
        super(String.format("User with ID '%d' not found", userId), ERROR_CODE, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message, ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

