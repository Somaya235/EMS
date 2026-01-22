package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.OtpCode;
import com.example.EMS_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository  extends JpaRepository<OtpCode, Long> {

  Optional<OtpCode> findByEmailAndOtp(String email, String otp);
  void deleteByEmail(String email);
}

