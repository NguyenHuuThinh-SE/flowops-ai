package com.flowops.identity.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record  RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 100)
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100)
        String password,

        @NotBlank(message = "Full name is required")
        @Size(max = 150)
        String fullName,

        @Size(max = 100)
        String department
) {
}
