package com.smartremind.payment_service.dto.provider;

import com.smartremind.payment_service.enums.Currency;
import com.smartremind.payment_service.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentProviderRequestDTO(

        String paymentId ,
        BigDecimal amount ,
        PaymentMethod paymentMethod,
        Currency currency


) {
}
