package com.example.demo.security;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long REFRESH_EXPIRATION_DAYS = 7;

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        return refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(UUID.randomUUID().toString());
                    existingToken.setExpiryDate(
                            LocalDateTime.now().plusDays(REFRESH_EXPIRATION_DAYS)
                    );
                    return refreshTokenRepository.save(existingToken);
                })
                .orElseGet(() -> {
                    RefreshToken refreshToken = RefreshToken.builder()
                            .user(user)
                            .token(UUID.randomUUID().toString())
                            .expiryDate(
                                    LocalDateTime.now().plusDays(REFRESH_EXPIRATION_DAYS)
                            )
                            .build();
                    return refreshTokenRepository.save(refreshToken);
                });
    }

    @Transactional
    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}

