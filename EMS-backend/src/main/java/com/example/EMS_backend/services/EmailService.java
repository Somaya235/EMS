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
}
