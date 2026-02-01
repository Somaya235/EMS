package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

public class ActivityNotFoundException extends BaseException {

    private static final String ERROR_CODE = "ACTIVITY_NOT_FOUND";

    public ActivityNotFoundException(Long activityId) {
        super(String.format("Student activity with ID '%d' not found", activityId), ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}
