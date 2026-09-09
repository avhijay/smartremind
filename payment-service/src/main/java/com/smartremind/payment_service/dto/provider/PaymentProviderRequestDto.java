package com.smartremind.payment_service.dto.provider;

import com.smartremind.payment_service.enums.Currency;

import java.math.BigDecimal;

public record PaymentProviderRequestDto(

        String paymentId ,
        BigDecimal amount ,
        Currency currency


) {
}
