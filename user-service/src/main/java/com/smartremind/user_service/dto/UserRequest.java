package com.smartremind.user_service.dto;

import com.smartremind.user_service.enums.Languages;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.enums.TimeZone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

public record UserRequest(

    @NotBlank
    String userName,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String firstName,

    String lastName,


    TimeZone timeZone,

    Languages languages,

    Boolean emailNotificationEnabled,

    Boolean smsNotificationEnabled,

    Boolean pushNotificationEnabled


){}

