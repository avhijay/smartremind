package com.smartremind.auth_service.controller;
import com.smartremind.common.ApiResponse;

import com.smartremind.auth_service.dto.RegistrationRequest;
import com.smartremind.auth_service.dto.UserResponse;
import com.smartremind.auth_service.userService.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth/register")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserResponse>registerUser(@Valid @RequestBody RegistrationRequest request){


        log.info("Request registerUser : received (UserController)");

        UserResponse response = userService.saveUser(request);

        URI location = URI.create("/login"+response.id());
        return ResponseEntity.created(location).body(response);

    }

}
