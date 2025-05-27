package com.driver.shifts.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driver.shifts.entity.RefreshToken;
import com.driver.shifts.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
