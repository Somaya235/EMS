package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique=true, nullable=false)
  private String name;
}

