package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="events")
public class  Event extends AuditableEntity {
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

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(LocalDateTime startAt) {
    this.startAt = startAt;
  }

  public LocalDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(LocalDateTime endAt) {
    this.endAt = endAt;
  }

  public StudentActivity getActivity() {
    return activity;
  }

  public void setActivity(StudentActivity activity) {
    this.activity = activity;
  }

  public Committee getCommittee() {
    return committee;
  }

  public void setCommittee(Committee committee) {
    this.committee = committee;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  public String getBannerImage() {
    return bannerImage;
  }

  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }

  public String getEventColor() {
    return eventColor;
  }

  public void setEventColor(String eventColor) {
    this.eventColor = eventColor;
  }
}
