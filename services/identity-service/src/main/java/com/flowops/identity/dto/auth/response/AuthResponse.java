package com.flowops.identity.dto.auth.response;

import java.util.Set;

public record  AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
    public record UserResponse(
            String id,
            String username,
            String email,
            String fullName,
            String department,
            Set<String> roles
    ) {
    }
}
