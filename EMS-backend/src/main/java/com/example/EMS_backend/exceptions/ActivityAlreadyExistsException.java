package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a student activity with the same name already exists.
 * Maps to HTTP 409 Conflict.
 */
public class ActivityAlreadyExistsException extends BaseException {

    private static final String ERROR_CODE = "ACTIVITY_ALREADY_EXISTS";

    public ActivityAlreadyExistsException(String activityName) {
        super(String.format("Student activity with name '%s' already exists", activityName),
                ERROR_CODE,
                HttpStatus.CONFLICT);
    }
}

