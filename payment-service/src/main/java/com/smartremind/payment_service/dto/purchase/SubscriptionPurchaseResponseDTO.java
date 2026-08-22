package com.smartremind.payment_service.dto.purchase;

import com.smartremind.payment_service.enums.PaymentStatus;

import com.smartremind.payment_service.enums.SubscriptionStatus;

import java.time.Instant;

public record SubscriptionPurchaseResponseDTO(

        String paymentId,
        Long subscriptionPlan,
        Boolean autoRenew,
        SubscriptionStatus status,
        PaymentStatus paymentStatus ,
        Instant expiryAt

) {
}
