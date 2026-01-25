package com.example.EMS_backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
  @Id 
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(unique=true, nullable=false)
  private String token;

  @Column(name = "expiry_date", nullable=false)
  private LocalDateTime expiryDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }
}

