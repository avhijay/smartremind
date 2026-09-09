package com.smartremind.payment_service.dto.provider;

import com.smartremind.payment_service.enums.Currency;
import com.smartremind.payment_service.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentProviderResponseDTO(

        //receipt
        String paymentId ,
        BigDecimal amountDue ,
        String providerOrderId,
        String status





) {
}
