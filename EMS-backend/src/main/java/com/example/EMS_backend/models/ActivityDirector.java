package com.example.EMS_backend.models;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="activity_directors")
public class ActivityDirector {

  @EmbeddedId
  private ActivityDirectorId id;

  @ManyToOne
  @MapsId("activityId")
  @JoinColumn(name="activity_id")
  private StudentActivity activity;

  @ManyToOne
  @MapsId("directorId")
  @JoinColumn(name="activity_director_id")
  private User director;

  @Column(name="job_desc")
  private String jobDesc;

  @Column(name="name")
  private String name;

  private LocalDateTime assignedAt = LocalDateTime.now();

  // Getters and Setters
  public ActivityDirectorId getId() {
    return id;
  }

  public void setId(ActivityDirectorId id) {
    this.id = id;
  }

  public StudentActivity getActivity() {
    return activity;
  }

  public void setActivity(StudentActivity activity) {
    this.activity = activity;
  }

  public User getDirector() {
    return director;
  }

  public void setDirector(User director) {
    this.director = director;
  }

  public String getJobDesc() {
    return jobDesc;
  }

  public void setJobDesc(String jobDesc) {
    this.jobDesc = jobDesc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getAssignedAt() {
    return assignedAt;
  }

  public void setAssignedAt(LocalDateTime assignedAt) {
    this.assignedAt = assignedAt;
  }
}
