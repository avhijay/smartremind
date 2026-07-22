package com.smartremind.payment_service.dto.provider;

import com.smartremind.payment_service.enums.PaymentStatus;

public record PaymentProviderResponseDTO(
        String providerTransactionId,
        String paymentId,
        PaymentStatus status

) {
}
