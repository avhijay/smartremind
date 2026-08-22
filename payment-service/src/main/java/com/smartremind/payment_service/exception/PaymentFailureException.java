package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailureException extends RuntimeException {
  private  final HttpStatus httpStatus;
    public PaymentFailureException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PaymentFailureException(String message){

      super(message);
      this.httpStatus =HttpStatus.UNPROCESSABLE_ENTITY;
    }

}
