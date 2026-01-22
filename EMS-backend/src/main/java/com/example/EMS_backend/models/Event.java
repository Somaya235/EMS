package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="events")
public class Event {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private LocalDateTime startAt;
  private LocalDateTime endAt;

  @ManyToOne
  @JoinColumn(name="stu_activity_id")
  private StudentActivity activity;

  @ManyToOne
  @JoinColumn(name="committee_id")
  private Committee committee;

  @ManyToOne
  @JoinColumn(name="created_by")
  private User createdBy;
}
