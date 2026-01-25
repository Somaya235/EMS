package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="events")
public class  Event {
  @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

  @Column(name="location")
  private String location;

  @Column(name="status")
  private String status;

  @ElementCollection
  @CollectionTable(name="event_tags", joinColumns=@JoinColumn(name="event_id"))
  @Column(name="tag")
  private Set<String> tags;

  @Column(name="banner_image")
  private String bannerImage;

  @Column(name="event_color")
  private String eventColor;
}
