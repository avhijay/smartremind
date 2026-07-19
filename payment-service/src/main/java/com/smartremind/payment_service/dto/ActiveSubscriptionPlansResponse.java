package com.smartremind.payment_service.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ActiveSubscriptionPlansResponse(
 Long id,
 String subscriptionPlan,
 BigDecimal amount,
 Integer planDurationDays,
 Boolean planIsActive



) {
}
