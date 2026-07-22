package com.smartremind.user_service.exception;



import com.smartremind.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.smartremind.common.exception.ErrorResponse;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static  final Logger log = LoggerFactory.getLogger (GlobalExceptionHandler.class);


    @ExceptionHandler(UserDoesNotExistException.class)

    public ResponseEntity< ErrorResponse> duplicateException(UserDoesNotExistException e , HttpServletRequest request){

        log.info ("Duplicate value found:{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("BAD request");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Duplicate entry found ");


        return ResponseEntity.badRequest().body(response);


    }



    @ExceptionHandler(ResourceNotFoundException.class)

    public ResponseEntity< ErrorResponse> ResourceNotFoundException(ResourceNotFoundException e , HttpServletRequest request){

        log.info ("Resource not found :{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Resource not found ");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage("Resource not found ");


        return ResponseEntity.badRequest().body(response);


    }


    @ExceptionHandler(InvalidPaginationException.class)

    public ResponseEntity< ErrorResponse> InvalidPaginationException (InvalidPaginationException e , HttpServletRequest request){

        log.info ("Invalid pagination request :{} " , e.getMessage());

        ErrorResponse response = new ErrorResponse();
        response.setError("Invalid pagination request ");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Invalid pagination request ");


        return ResponseEntity.badRequest().body(response);


    }


}
