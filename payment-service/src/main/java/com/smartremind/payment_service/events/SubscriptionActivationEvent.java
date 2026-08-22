package com.smartremind.payment_service.events;

import com.smartremind.payment_service.enums.SubscriptionStatus;

import java.time.Instant;

public record SubscriptionActivationEvent(

  String username ,
  SubscriptionStatus subscriptionStatus ,
  Instant subscriptionExpiryDate,

  // add these two fields in consumer and configure accordingly
  String uniqueId ,
  Long subscriptionPlanId




) {
}
