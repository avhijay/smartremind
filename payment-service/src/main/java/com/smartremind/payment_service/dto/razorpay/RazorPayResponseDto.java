package com.smartremind.payment_service.dto.razorpay;

import com.smartremind.payment_service.enums.Currency;

import java.util.List;

public record RazorPayResponseDto(
        Long amount,
        Long amountDue,
        Long amountPaid,
        Integer attempts,
        Long createdAt,
        Currency currency,
        String entity,
        String id,
        List<?> notes,
        String offerId,
        String receipt,
        String status



) {
}
