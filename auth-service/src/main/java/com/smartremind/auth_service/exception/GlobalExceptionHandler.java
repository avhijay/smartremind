package com.smartremind.auth_service.exception;



import com.smartremind.common.exception.ErrorResponse;
import com.smartremind.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.smartremind.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> validationException(ValidationException e , HttpServletRequest request){



log.error("Registration / login request failed {}" , e.getMessage());
      return  ResponseEntity.badRequest().body(ApiResponse.error("Validation Failed"));

    }


}
