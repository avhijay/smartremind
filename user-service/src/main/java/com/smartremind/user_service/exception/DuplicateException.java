package com.smartremind.user_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends RuntimeException {
    private final HttpStatus httpStatus;


    public DuplicateException( String message,HttpStatus status){

        super(message);
        this.httpStatus = status;

    }

    public DuplicateException(String message) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
    }


}
