package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.PaymentStatus;

public record PaymentProviderResponse(
        String providerTransactionId,
        String paymentId,
        PaymentStatus status

) {
}
