package com.smartremind.auth_service.dto;

import com.smartremind.auth_service.enums.Role;

public record UserResponse(

        Long id,
        String username,
        Role role,
        boolean enabled


) {
}
