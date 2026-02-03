package com.example.EMS_backend.dto;

import jakarta.validation.constraints.*;

/**
 * Request DTO for creating or updating committees.
 */
public class CommitteeRequestDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Activity ID is required")
    private Long activityId;
    
    private Long directorId; // Optional
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
}
