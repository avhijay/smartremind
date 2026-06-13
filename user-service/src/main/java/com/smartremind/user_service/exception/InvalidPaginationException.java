package com.smartremind.user_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidPaginationException extends RuntimeException {

    private final HttpStatus status;

    public InvalidPaginationException(HttpStatus status , String message){
        super(message);
        this.status=status;

    }


    public InvalidPaginationException(String message) {
        super(message);

        this.status=HttpStatus.BAD_REQUEST;
    }
}
