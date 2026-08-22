package com.smartremind.payment_service.dto.purchase;

import com.smartremind.payment_service.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;

public record SubscriptionPurchaseRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        Long subscriptionPlan,
        @NotBlank
        Boolean autoRenew,
        @NotBlank
        PaymentMethod paymentMethod


) {
}
