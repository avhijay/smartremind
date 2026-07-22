package com.smartremind.payment_service.dto;

import java.math.BigDecimal;

public record ActiveSubscriptionPlansResponseDTO(
 Long id,
 String subscriptionPlan,
 BigDecimal amount,
 Integer planDurationDays,
 Boolean planIsActive



) {
}
