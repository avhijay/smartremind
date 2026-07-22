package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class PaymentRetryExhaustedException extends RuntimeException {

    private  final HttpStatus httpStatus;

    public PaymentRetryExhaustedException(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PaymentRetryExhaustedException(String message){
        super(message);
        this.httpStatus=HttpStatus.UNPROCESSABLE_ENTITY;

    }

}
