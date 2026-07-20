package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.PaymentStatus;

import com.smartremind.payment_service.enums.SubscriptionStatus;

import java.time.Instant;

public record SubscriptionPurchaseResponse(

        String paymentId,
        String subscriptionPlan,
        Boolean autoRenew,
        SubscriptionStatus status,
        PaymentStatus paymentStatus ,
        Instant expiryAt

) {
}
