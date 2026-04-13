package com.smartremind.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceCommunicationException extends RuntimeException {

    private final HttpStatus status;

    public ServiceCommunicationException(String message , HttpStatus status) {
        super(message);
        this.status=status;
    }

    public ServiceCommunicationException(String message){
        super(message);
        this.status= HttpStatus.SERVICE_UNAVAILABLE;
    }
}
