package com.example.EMS_backend.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Response DTO for event data.
 * Excludes sensitive information and provides clean API contract.
 */
public class EventResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private StudentActivityResponseDTO activity;
    private CommitteeResponseDTO committee;
    private String location;
    private String status;
    private Set<String> tags;
    private String bannerImage;
    private String eventColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
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
    
    public StudentActivityResponseDTO getActivity() {
        return activity;
    }
    
    public void setActivity(StudentActivityResponseDTO activity) {
        this.activity = activity;
    }
    
    public CommitteeResponseDTO getCommittee() {
        return committee;
    }
    
    public void setCommittee(CommitteeResponseDTO committee) {
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
