package com.smartremind.user_service.event;

import com.smartremind.user_service.enums.SubscriptionStatus;

import java.time.Instant;

public record SubscriptionActivationEvent(
        String username ,
        SubscriptionStatus subscriptionStatus ,
        Instant subscriptionExpiryDate

) {
}
