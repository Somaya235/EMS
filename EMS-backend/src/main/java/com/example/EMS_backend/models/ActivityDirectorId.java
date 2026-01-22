package com.example.EMS_backend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ActivityDirectorId implements Serializable {
  private Long activityId;
  private Long directorId;
}
