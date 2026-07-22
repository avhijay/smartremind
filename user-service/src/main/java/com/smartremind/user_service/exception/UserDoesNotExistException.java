package com.smartremind.user_service.exception;

import org.springframework.http.HttpStatus;

public class UserDoesNotExistException extends RuntimeException {
    private final HttpStatus httpStatus;


    public UserDoesNotExistException(String message, HttpStatus status){

        super(message);
        this.httpStatus = status;

    }

    public UserDoesNotExistException(String message) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
    }


}