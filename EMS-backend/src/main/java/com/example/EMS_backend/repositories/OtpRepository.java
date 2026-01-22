package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.OtpCode;
import com.example.EMS_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpCode, Long> {
  Optional<OtpCode> findByUser(User user);
  Optional<OtpCode> findByUserId(Long userId);
  void deleteByUserId(Long userId);
}

