package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique=true, nullable=false)
  private String name;

  private LocalDateTime createdAt = LocalDateTime.now();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}

