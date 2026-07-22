package com.smartremind.payment_service.exception;

import org.springframework.http.HttpStatus;

public class PaymentDoesNotExistException extends RuntimeException {

  private final HttpStatus httpStatus;

    public PaymentDoesNotExistException(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }

    public PaymentDoesNotExistException(String message){
      super(message);
      this.httpStatus = HttpStatus.NOT_FOUND;

    }

}
