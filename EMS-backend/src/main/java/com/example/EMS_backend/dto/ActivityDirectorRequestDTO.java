package com.example.EMS_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for assigning or updating an Activity Director for a Student Activity.
 */
public class ActivityDirectorRequestDTO {

    @NotNull(message = "Activity Director ID is required")
    private Long directorId;

    @NotBlank(message = "Position name is required")
    @Size(max = 255, message = "Position name must not exceed 255 characters")
    private String positionName;

    @Size(max = 2000, message = "Job description must not exceed 2000 characters")
    private String jobDescription;

    // Getters and Setters
    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
