package com.smartremind.auth_service.events;

public record UserCreationEvent(
        String username,
        String email

) {
}
