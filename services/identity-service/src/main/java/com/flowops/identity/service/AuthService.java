package com.flowops.identity.service;

import com.flowops.identity.dto.auth.request.LoginRequest;
import com.flowops.identity.dto.auth.request.RegisterRequest;
import com.flowops.identity.dto.auth.response.AuthResponse;
import com.flowops.identity.entity.Role;
import com.flowops.identity.entity.User;
import com.flowops.identity.repository.RoleRepository;
import com.flowops.identity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("User already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new IllegalStateException("Default role does not exist"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .department(request.department())
                .status("ACTIVE")
                .roles(Set.of(employeeRole))
                .build();

        User savedUser = userRepository.save(user);

        return createAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid username or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Invalid username or password"
            );
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalStateException(
                    "User account is not active"
            );
        }

        return createAuthResponse(user);
    }


    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Set<String> roles = user.getRoles()
                .stream().map(Role::getName)
                .collect(Collectors.toSet());

        AuthResponse.UserResponse userResponse = new AuthResponse.UserResponse(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getDepartment(),
                roles
        );

        return new AuthResponse(accessToken,
                refreshToken,
                "Bearer",
                900,
                userResponse);
    }
}
