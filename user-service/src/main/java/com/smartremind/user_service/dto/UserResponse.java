package com.smartremind.user_service.dto;

import com.smartremind.user_service.enums.Languages;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.enums.TimeZone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;


public record UserResponse(

@NotNull
        Long id,

        @NotNull
        String userName,

        @NotBlank
        @Email
        String email,

        @NotNull
        String firstName,

        String lastName,

        @NotNull
        SubscriptionStatus status,

      Instant subscriptionExpiryDate,

        TimeZone timeZone,

        Languages languages,

        Boolean emailNotificationEnabled,

        Boolean smsNotificationEnabled,

        Boolean pushNotificationEnabled,

       Instant createdAt,

        Instant updatedAt



) {
}
