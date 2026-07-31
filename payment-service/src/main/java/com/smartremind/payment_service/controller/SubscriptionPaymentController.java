package com.smartremind.payment_service.controller;


import com.smartremind.payment_service.dto.ActiveSubscriptionPlansResponseDTO;
import com.smartremind.payment_service.dto.CurrentUserResponseDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseRequestDTO;
import com.smartremind.payment_service.dto.purchase.SubscriptionPurchaseResponseDTO;
import com.smartremind.payment_service.service.SubscriptionPaymentService;
import com.smartremind.payment_service.service.SubscriptionPlansService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        log.info("Request get current User : Received ");

        CurrentUserResponseDTO response = new CurrentUserResponseDTO(username,role);
        return  ResponseEntity.ok(response);
    }


    @GetMapping("/active/plans")
    public ResponseEntity<List<ActiveSubscriptionPlansResponseDTO>> getActivePlans(){
        List<ActiveSubscriptionPlansResponseDTO> responses = subscriptionPlansService.getActivePlans();

        return ResponseEntity.ok(responses);
    }


    @PostMapping("{idempotencyKey}")
    public ResponseEntity<SubscriptionPurchaseResponseDTO>requestPayment
            (@Valid @RequestBody SubscriptionPurchaseRequestDTO subscriptionPurchaseRequestDTO , @RequestParam String idempotencyKey ){

        SubscriptionPurchaseResponseDTO responseDTO = subscriptionPaymentService.createPayment(subscriptionPurchaseRequestDTO , idempotencyKey);

        return ResponseEntity.ok(responseDTO);


    }

}
