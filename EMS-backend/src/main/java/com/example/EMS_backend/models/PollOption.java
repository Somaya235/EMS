package com.example.EMS_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name="poll_options")
public class PollOption {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String optionText;

  @ManyToOne
  @JoinColumn(name="poll_id")
  private Poll poll;
}
