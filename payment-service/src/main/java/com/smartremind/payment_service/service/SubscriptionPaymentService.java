package com.smartremind.payment_service.service;

import com.smartremind.payment_service.dto.SubscriptionPurchaseRequest;
import com.smartremind.payment_service.dto.SubscriptionPurchaseResponse;
import com.smartremind.payment_service.entity.SubscriptionPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPaymentService {

    private static  final String PAYMENT_ID = "Payment-";
    private static  final int maxRetryAllowed = 3;

    private static  final Logger log = LoggerFactory.getLogger(SubscriptionPaymentService.class);

    private final SubscriptionPaymentService paymentService ;

    public  SubscriptionPaymentService(SubscriptionPaymentService paymentService){
        this.paymentService = paymentService;
    }
private boolean retryPayment(String paymentId){
        int retryAllowed = maxRetryAllowed;


}



















    private SubscriptionPurchaseResponse purchaseToResponseHelper(SubscriptionPayment subscriptionPayment){
        return new SubscriptionPurchaseResponse(subscriptionPayment.getPaymentId(),
                subscriptionPayment.getSubscriptionPlan(),subscriptionPayment.getAutoRenew()
                ,subscriptionPayment.getSubscriptionStatus(),subscriptionPayment.getPaymentStatus(), subscriptionPayment.getExpiresAt());

    }




}
