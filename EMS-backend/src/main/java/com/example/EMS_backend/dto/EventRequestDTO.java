package com.example.EMS_backend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Request DTO for creating or updating events.
 */
public class EventRequestDTO {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startAt;
    
    @NotNull(message = "End time is required")
    private LocalDateTime endAt;
    
    private Long activityId;
    
    private Long committeeId;
    
    @Size(max = 500, message = "Location must not exceed 500 characters")
    private String location;
    
    @Pattern(regexp = "DRAFT|PUBLISHED|CANCELLED|COMPLETED", message = "Invalid status")
    private String status;
    
    private Set<String> tags;
    
    private String bannerImage;
    
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid color format")
    private String eventColor;
    
    // Getters and Setters
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
    
    public Long getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public Long getCommitteeId() {
        return committeeId;
    }
    
    public void setCommitteeId(Long committeeId) {
        this.committeeId = committeeId;
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
