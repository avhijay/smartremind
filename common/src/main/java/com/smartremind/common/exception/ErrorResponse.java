package com.smartremind.common.exception;


import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;

    //@Builder.Default
     private LocalDateTime localDateTime = LocalDateTime.now();

}
