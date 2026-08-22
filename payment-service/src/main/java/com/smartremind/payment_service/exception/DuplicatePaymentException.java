package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicatePaymentException extends RuntimeException {

    private  final HttpStatus httpStatus;

    public DuplicatePaymentException(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }


    public DuplicatePaymentException(String message){

        super("Payment already present :" + message);
        this.httpStatus = HttpStatus.CONFLICT;
    }


}
