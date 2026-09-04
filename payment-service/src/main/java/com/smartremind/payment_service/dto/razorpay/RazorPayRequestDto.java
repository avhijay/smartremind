package com.smartremind.payment_service.dto.razorpay;

import com.smartremind.payment_service.enums.Currency;

public record RazorPayRequestDto(

        Long amount,
        Currency currency,
        String receipt

) {
}
