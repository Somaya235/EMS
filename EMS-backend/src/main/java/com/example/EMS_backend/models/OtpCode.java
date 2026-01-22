package com.example.EMS_backend.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OtpCode {
  @Id @GeneratedValue
  private Long id;

  @OneToOne
  private User user;

  private String code;
  private LocalDateTime expiry;
  private boolean verified;
}
