package com.driver.shifts.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.driver.shifts.entity.RefreshToken;
import com.driver.shifts.entity.User;
import com.driver.shifts.repositories.RefreshTokenRepository;
import com.driver.shifts.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + userEmail + " not found");
        }

        User user = userOpt.get();

        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);

        if (existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();

            if (existingToken.getExpiryDate().isBefore(Instant.now())) {
            	
                existingToken.setToken(UUID.randomUUID().toString());
                existingToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
                return refreshTokenRepository.save(existingToken);
            } else {
                return existingToken;
            }
        } else {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUserEmail(userEmail);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setUser(user);
            return refreshTokenRepository.save(refreshToken);
        }
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElse(null);
    }
}
