package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExistException extends RuntimeException {

    private  final HttpStatus httpStatus;
    public SubscriptionAlreadyExistException(String message  ,  HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;

    }

    public SubscriptionAlreadyExistException(String message){

        super("Already subscribed to the  given plan : "+message);


        this.httpStatus=HttpStatus.CONFLICT;
    }

}
