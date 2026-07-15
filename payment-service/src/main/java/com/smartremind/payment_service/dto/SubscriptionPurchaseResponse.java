package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.PaymentStatus;
import com.smartremind.payment_service.enums.SubscriptionPlan;

public record SubscriptionPurchaseResponse(

        String paymentId,
        SubscriptionPlan subscriptionPlan,
        Boolean autoRenew,
        String subscriptionStatus,
        PaymentStatus paymentStatus ,
        String expiryAt

) {
}
