package com.smartremind.payment_service.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.smartremind.common.exception.ErrorResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {


    private  static  final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<ErrorResponse> duplicateException(DuplicatePaymentException e , HttpServletRequest request){


        log.info ("Duplicate  Payment found :{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Can not create Duplicate Payment ");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setMessage("Payment With the same Key Already exist ");


        return ResponseEntity.badRequest().body(response);


    }



    @ExceptionHandler(PaymentDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> paymentDoesNotExistException(PaymentDoesNotExistException e , HttpServletRequest request){


        log.info ("  Payment not found found :{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Payment does not exist  ");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage("Payment With the provided id not found  ");


        return ResponseEntity.badRequest().body(response);


    }



    @ExceptionHandler(PaymentFailureException.class)
    public ResponseEntity<ErrorResponse> paymentFailureException(PaymentFailureException e , HttpServletRequest request){


        log.info ("Payment order cannot be created  :{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Error in  Payment order creation ");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Payment order cannot be created : internal server error  ");


        return ResponseEntity.badRequest().body(response);


    }



}
