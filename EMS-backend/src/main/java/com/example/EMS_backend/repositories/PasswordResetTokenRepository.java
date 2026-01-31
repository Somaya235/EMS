package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.PasswordResetToken;
import com.example.EMS_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserAndUsedFalse(User user);

    boolean existsByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now OR t.used = true")
    void deleteExpiredOrUsedTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.used = true WHERE t.user = :user")
    void markAllTokensForUserAsUsed(@Param("user") User user);

    @Query("SELECT t FROM PasswordResetToken t WHERE t.user = :user AND t.used = false AND t.expiryDate > :now")
    Optional<PasswordResetToken> findValidTokenByUser(@Param("user") User user, @Param("now") LocalDateTime now);
}
