package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;

public record SubscriptionPurchaseRequest(
        @NotBlank
        String username,
        @NotBlank
        String subscriptionPlan,
        @NotBlank
        Boolean autoRenew,
        @NotBlank
        PaymentMethod paymentMethod


) {
}
