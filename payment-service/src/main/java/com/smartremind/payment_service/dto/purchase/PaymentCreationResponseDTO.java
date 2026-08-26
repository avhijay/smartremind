package com.smartremind.payment_service.dto.purchase;

import com.smartremind.payment_service.enums.PaymentStatus;

public record PaymentCreationResponseDTO(
        String paymentId,
        PaymentStatus status

) {
}
