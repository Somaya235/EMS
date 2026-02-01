package com.example.EMS_backend.services;

import com.example.EMS_backend.dto.ForgotPasswordRequest;
import com.example.EMS_backend.dto.ResetPasswordRequest;
import com.example.EMS_backend.exceptions.ResourceNotFoundException;
import com.example.EMS_backend.exceptions.TokenInvalidException;
import com.example.EMS_backend.models.PasswordResetToken;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.PasswordResetTokenRepository;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${app.password-reset.token-expiration-minutes:15}")
    private int tokenExpirationMinutes;

    public void initiatePasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email '" + request.getEmail() + "' not found"));

        if (!user.getEnabled()) {
            throw new IllegalStateException("Account is not enabled. Please contact support.");
        }

        passwordResetTokenRepository.markAllTokensForUserAsUsed(user);

        String resetToken = generateResetToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenExpirationMinutes);

        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, user, expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getFullName(),
                resetToken,
                frontendUrl
        );
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.isPasswordMatching()) {
            throw new IllegalArgumentException("Password and confirmation do not match");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new TokenInvalidException("Invalid or expired reset token"));

        if (!resetToken.isValid()) {
            throw new TokenInvalidException("Token is expired or has been used");
        }

        User user = resetToken.getUser();

        if (!user.getEnabled()) {
            throw new IllegalStateException("Account is not enabled. Please contact support.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPasswordHash(encodedNewPassword);
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        passwordResetTokenRepository.markAllTokensForUserAsUsed(user);

        emailService.sendPasswordResetConfirmation(user.getEmail(), user.getFullName());
    }

    public boolean validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidException("Invalid reset token"));

        return resetToken.isValid();
    }

    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteExpiredOrUsedTokens(LocalDateTime.now());
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
