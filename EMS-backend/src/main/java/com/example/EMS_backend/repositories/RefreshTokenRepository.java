package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  Optional<RefreshToken> findByUserId(Long userId);
  void deleteByUserId(Long userId);
}
