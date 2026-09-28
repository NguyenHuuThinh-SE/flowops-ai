package com.flowops.identity.controller;

import com.flowops.identity.dto.auth.LogoutRequest;
import com.flowops.identity.dto.auth.request.LoginRequest;
import com.flowops.identity.dto.auth.request.RefreshTokenRequest;
import com.flowops.identity.dto.auth.request.RegisterRequest;
import com.flowops.identity.dto.auth.response.AuthResponse;
import com.flowops.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(
                request.refreshToken()
        );
    }

    @PostMapping("/logout")
    public void logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        authService.logout(
                request.refreshToken()
        );
    }

}
