package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is already assigned as president of another activity.
 * Maps to HTTP 409 Conflict.
 */
public class PresidentAlreadyAssignedException extends BaseException {

    private static final String ERROR_CODE = "PRESIDENT_ALREADY_ASSIGNED";

    public PresidentAlreadyAssignedException(Long userId) {
        super(String.format("User with ID '%d' is already assigned as president of another activity", userId),
                ERROR_CODE,
                HttpStatus.CONFLICT);
    }
}

