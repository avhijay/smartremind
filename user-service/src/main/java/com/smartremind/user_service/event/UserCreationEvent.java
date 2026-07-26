package com.smartremind.user_service.event;

public record UserCreationEvent(
        String username,
        String email

) {
}
