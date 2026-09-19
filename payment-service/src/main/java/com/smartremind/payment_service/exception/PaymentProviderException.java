package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class PaymentProviderException extends RuntimeException {
    private  final HttpStatus httpStatus;

    public PaymentProviderException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public PaymentProviderException (String message , HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;

    }
}
