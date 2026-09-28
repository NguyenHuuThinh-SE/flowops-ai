package com.flowops.identity.service;

import com.flowops.identity.entity.RefreshToken;
import com.flowops.identity.entity.User;
import com.flowops.identity.repository.RefreshTokenRepository;
import com.flowops.identity.repository.UserRepository;
import com.flowops.identity.util.TokenHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public String createRefreshToken(User user) {

        String rawToken =
                jwtService.generateRefreshToken(user);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .userId(user.getId())
                        .tokenHash(
                                TokenHashUtil.sha256(rawToken)
                        )
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )
                        .revoked(false)
                        .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Transactional()
    public User validateAndGetUser(String rawToken) {

        String tokenHash =
                TokenHashUtil.sha256(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Refresh token has expired"
            );
        }

        return userRepository
                .findById(refreshToken.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
    }

    @Transactional
    public void revokeToken(String rawToken) {

        String tokenHash =
                TokenHashUtil.sha256(rawToken);

        refreshTokenRepository
                .findByTokenHash(tokenHash)
                .ifPresent(token -> {

                    token.setRevoked(true);

                    refreshTokenRepository.save(token);
                });
    }
}
