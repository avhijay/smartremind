package com.smartremind.payment_service.controller;


import com.smartremind.payment_service.dto.ActiveSubscriptionPlansResponseDTO;
import com.smartremind.payment_service.dto.CurrentUserResponseDTO;
import com.smartremind.payment_service.service.SubscriptionPaymentService;
import com.smartremind.payment_service.service.SubscriptionPlansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionPaymentController {


    private static final Logger log = LoggerFactory.getLogger(SubscriptionPaymentController.class);
    private final SubscriptionPaymentService subscriptionPaymentService;
    private final SubscriptionPlansService subscriptionPlansService;

    public SubscriptionPaymentController(SubscriptionPaymentService subscriptionPaymentService , SubscriptionPlansService subscriptionPlansService){
        this.subscriptionPaymentService = subscriptionPaymentService;
        this.subscriptionPlansService =subscriptionPlansService;


    }


    @GetMapping("/current/user")
public ResponseEntity<CurrentUserResponseDTO>getCurrentUser(@RequestHeader("X-User-Name") String username , @RequestHeader("X-User-Roles") String role){

        CurrentUserResponseDTO response = new CurrentUserResponseDTO(username,role);
        return  ResponseEntity.ok(response);
    }


    @GetMapping("/active/plans")
    public ResponseEntity<List<ActiveSubscriptionPlansResponseDTO>> getActivePlans(){
        List<ActiveSubscriptionPlansResponseDTO> responses = subscriptionPlansService.getActivePlans();

        return ResponseEntity.ok(responses);
    }

}
