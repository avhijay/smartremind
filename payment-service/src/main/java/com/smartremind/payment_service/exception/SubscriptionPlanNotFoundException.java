package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionPlanNotFoundException extends RuntimeException {
    private  final HttpStatus httpStatus;

    public SubscriptionPlanNotFoundException(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SubscriptionPlanNotFoundException(String message ){
        super(message);
        this.httpStatus= HttpStatus.NOT_FOUND;
    }
}
