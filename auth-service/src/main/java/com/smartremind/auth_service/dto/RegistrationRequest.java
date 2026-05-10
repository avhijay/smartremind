package com.smartremind.auth_service.dto;

import com.smartremind.auth_service.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RegistrationRequest(
@NotBlank(message = "username required ")
        String username,
        @NotBlank(message = "password cannot be empty")
        String password




) {
}
