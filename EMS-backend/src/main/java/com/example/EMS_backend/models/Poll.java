package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="polls")
public class Poll {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private Boolean isPublic;

  @ManyToOne
  @JoinColumn(name="event_id")
  private Event event;
}
