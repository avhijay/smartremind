package com.smartremind.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends RuntimeException {

    private final HttpStatus status;

    public ValidationException(String message , HttpStatus status) {
        super(message);

        this.status= status;
    }

    public ValidationException(String message){
        super(message);
        this.status= HttpStatus.BAD_REQUEST;
    }


}
