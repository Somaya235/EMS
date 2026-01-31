package com.example.EMS_backend.services;

import com.example.EMS_backend.dto.JwtResponse;
import com.example.EMS_backend.dto.LoginRequest;
import com.example.EMS_backend.dto.OtpRequest;
import com.example.EMS_backend.dto.RegisterRequest;
import com.example.EMS_backend.models.OtpCode;
import com.example.EMS_backend.models.RefreshToken;
import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.OtpRepository;
import com.example.EMS_backend.repositories.RefreshTokenRepository;
import com.example.EMS_backend.repositories.RoleRepository;
import com.example.EMS_backend.repositories.UserRepository;
import com.example.EMS_backend.security.JwtUtils;
import com.example.EMS_backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            // If user exists but is not yet verified, resend OTP instead of blocking registration
            User existingUser = userRepository.findByEmail(registerRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (Boolean.FALSE.equals(existingUser.getEnabled())) {
                // Clear any old OTPs and send a new one
                resendOtp(registerRequest.getEmail());
                return existingUser;
            }

            // Already verified → email really in use
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);

        Role memberRole = roleRepository.findByName("member")
                .orElseThrow(() -> new RuntimeException("Role not found: member"));
        user.getRoles().add(memberRole);

        User savedUser = userRepository.save(user);

        String otp = generateOtp();
        OtpCode otpCode = new OtpCode();
        otpCode.setUser(savedUser);
        otpCode.setCode(otp);
        otpCode.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        otpCode.setVerified(false);

        otpRepository.save(otpCode);
        try {
            emailService.sendOtpEmail(savedUser.getEmail(), otp);
        } catch (MailException e) {
            throw new RuntimeException("Unable to send OTP email. Please check mail configuration and try again.", e);
        }

        return savedUser;
    }

    public String verifyOtp(OtpRequest otpRequest) {
        User user = userRepository.findByEmail(otpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OtpCode otpCode = otpRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpCode);
            throw new RuntimeException("OTP has expired");
        }

        if (!otpCode.getCode().equals(otpRequest.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setEnabled(true);
        userRepository.save(user);

        otpRepository.delete(otpCode);

        emailService.sendRegistrationConfirmation(user.getEmail(), user.getFullName());

        return "Email verified successfully. You can now login.";
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(authentication);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getFullName(),
                    userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(java.util.stream.Collectors.toList()));
        } catch (Exception e) {
            throw e;
        }
    }

    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEnabled()) {
            throw new RuntimeException("Account is already verified");
        }

        otpRepository.deleteByUserId(user.getId());

        String otp = generateOtp();
        OtpCode otpCode = new OtpCode();
        otpCode.setUser(user);
        otpCode.setCode(otp);
        otpCode.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        otpCode.setVerified(false);

        otpRepository.save(otpCode);
        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (MailException e) {
            throw new RuntimeException("Unable to send OTP email. Please check mail configuration and try again.", e);
        }

        return "OTP has been resent to your email";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
