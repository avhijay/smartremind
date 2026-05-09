package com.smartremind.auth_service.dto;

import com.smartremind.auth_service.enums.Role;
import jakarta.validation.constraints.NotNull;


public record RegistrationRequest(
@NotNull
        String username,
        @NotNull
        String password




) {
}
