package com.smartremind.payment_service.dto;

import com.smartremind.payment_service.enums.Currency;
import com.smartremind.payment_service.enums.PaymentMethod;
import com.smartremind.payment_service.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentProviderRequest(

        String paymentId ,
        BigDecimal amount ,
        PaymentMethod paymentMethod,
        Currency currency


) {
}
