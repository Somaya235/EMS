package com.example.EMS_backend.models;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="full_name", nullable=false)
  private String fullName;

  @Column(nullable=false, unique=true)
  private String email;

  @Column(name="password_hash", nullable=false)
  private String passwordHash;

  private LocalDateTime createdAt = LocalDateTime.now();

  // Roles (M:N)
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name="user_roles",
    joinColumns=@JoinColumn(name="user_id"),
    inverseJoinColumns=@JoinColumn(name="role_id")
  )
  private Set<Role> roles = new HashSet<>();
}
