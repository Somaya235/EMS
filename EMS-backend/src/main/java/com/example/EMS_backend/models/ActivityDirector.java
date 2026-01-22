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

  private LocalDateTime assignedAt = LocalDateTime.now();
}
