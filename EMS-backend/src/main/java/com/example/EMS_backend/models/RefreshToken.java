package com.example.EMS_backend.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RefreshToken {
  @Id @GeneratedValue
  private Long id;

  @OneToOne
  private User user;

  @Column(unique=true)
  private String token;

  private LocalDateTime expiry;
}

