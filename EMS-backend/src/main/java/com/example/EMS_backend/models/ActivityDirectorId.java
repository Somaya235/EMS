package com.example.EMS_backend.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ActivityDirectorId implements Serializable {
  private Long activityId;
  private Long directorId;

  public ActivityDirectorId() {}

  public ActivityDirectorId(Long activityId, Long directorId) {
    this.activityId = activityId;
    this.directorId = directorId;
  }

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public Long getDirectorId() {
    return directorId;
  }

  public void setDirectorId(Long directorId) {
    this.directorId = directorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ActivityDirectorId that = (ActivityDirectorId) o;
    return Objects.equals(activityId, that.activityId) &&
            Objects.equals(directorId, that.directorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityId, directorId);
  }
}
