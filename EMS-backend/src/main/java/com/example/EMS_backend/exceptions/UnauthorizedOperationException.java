package com.example.EMS_backend.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedOperationException extends BaseException {

    private static final String ERROR_CODE = "UNAUTHORIZED_OPERATION";

    public UnauthorizedOperationException(String message) {
        super(message, ERROR_CODE, HttpStatus.FORBIDDEN);
    }
}
