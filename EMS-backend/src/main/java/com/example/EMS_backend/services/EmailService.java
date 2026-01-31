package com.example.EMS_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("EMS - Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp + "\n\n" +
                "This OTP will expire in 5 minutes.\n" +
                "Please do not share this OTP with anyone.");
        
        mailSender.send(message);
    }

    public void sendRegistrationConfirmation(String toEmail, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to EMS - Registration Successful");
        message.setText("Dear " + fullName + ",\n\n" +
                "Your registration has been successfully completed!\n" +
                "You can now login to your account.\n\n" +
                "Best regards,\n" +
                "EMS Team");
        
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String fullName, String resetToken, String baseUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("EMS - Password Reset Request");
        
        String resetLink = baseUrl + "/reset-password?token=" + resetToken;
        
        message.setText("Dear " + fullName + ",\n\n" +
                "We received a request to reset your password for your EMS account.\n\n" +
                "Click the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "This link will expire in 15 minutes for security reasons.\n" +
                "If you didn't request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "EMS Team");
        
        mailSender.send(message);
    }

    public void sendPasswordResetConfirmation(String toEmail, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("EMS - Password Reset Successful");
        message.setText("Dear " + fullName + ",\n\n" +
                "Your password has been successfully reset!\n" +
                "You can now login to your account with your new password.\n\n" +
                "If you didn't make this change, please contact our support team immediately.\n\n" +
                "Best regards,\n" +
                "EMS Team");
        
        mailSender.send(message);
    }
}
