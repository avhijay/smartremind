package com.smartremind.user_service.controller;


import com.smartremind.user_service.dto.UserRequest;
import com.smartremind.user_service.dto.UserResponse;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {


    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService ;


    public UserController(UserService userService){
        this.userService = userService;

    }



@PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){

        log.info("Create user received : user- controller");


        UserResponse response = userService.createUser(request);

        URI location = URI.create("/user"+response.id());

        return ResponseEntity.created(location).body(response);


    }


    @GetMapping("/{userid}")

    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userid){

        log.info("Get user by Id received : user - controller");

        UserResponse userResponse = userService.getUserById(userid);

        return ResponseEntity.ok(userResponse);



    }



    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){

        log.info("Get user by email received : user - controller");

        UserResponse  userResponse = userService.getUserByEmail(email);

        return ResponseEntity.ok(userResponse);


    }




    @GetMapping
    public ResponseEntity<UserResponse>getUserByName(@RequestParam String name){

        log.info("Get user by name received : user - controller");

        UserResponse response = userService.getUserByName(name);
        return  ResponseEntity.ok(response);


    }

    @GetMapping("/status")
    public ResponseEntity<Page<UserResponse>>getUserByStatus(@RequestParam SubscriptionStatus status , Pageable pageable){

        log.info("Get users by status received : user - controller");

        Page<UserResponse> responses = userService.getUserBySubscriptionStatus(status , pageable);


        return  ResponseEntity.ok(responses);



    }



}
