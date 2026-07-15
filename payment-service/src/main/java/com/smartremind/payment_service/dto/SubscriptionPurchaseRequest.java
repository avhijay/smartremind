package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.PaymentMethod;
import com.smartremind.payment_service.enums.SubscriptionPlan;
import jakarta.validation.constraints.NotBlank;

public record SubscriptionPurchaseRequest(
        @NotBlank
        String username,
        @NotBlank
        SubscriptionPlan subscriptionPlan,
        @NotBlank
        Boolean autoRenew,
        @NotBlank
        PaymentMethod paymentMethod


) {
}
