package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.*;
import com.example.EMS_backend.exceptions.TokenRefreshException;
import com.example.EMS_backend.models.RefreshToken;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.security.JwtUtils;
import com.example.EMS_backend.security.UserDetailsImpl;
import com.example.EMS_backend.services.AuthService;
import com.example.EMS_backend.services.PasswordResetService;
import com.example.EMS_backend.services.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

//    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordResetService passwordResetService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        try {
            System.out.println("Registering user: " + signUpRequest);
            User user = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for OTP verification."));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unable to send otp email")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error: " + e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpRequest otpRequest) {
        try {
            String message = authService.verifyOtp(otpRequest);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unable to send")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error: " + e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        try {
            String message = authService.resendOtp(email);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unable to send otp email")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error: " + e.getMessage()));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new JwtResponse(token,
                            user.getId(),
                            user.getEmail(),
                            user.getFullName(),
                            user.getRoles().stream()
                                    .map(role -> role.getName())
                                    .collect(java.util.stream.Collectors.toList())));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            refreshTokenService.deleteByUserId(userDetails.getId());
        }
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.initiatePasswordReset(request);
            return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = passwordResetService.validateResetToken(token);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            response.put("message", isValid ? "Token is valid" : "Token is invalid or expired");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
