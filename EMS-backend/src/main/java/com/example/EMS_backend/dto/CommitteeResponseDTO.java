package com.example.EMS_backend.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for committee data.
 */
public class CommitteeResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private StudentActivityResponseDTO activity;
    private UserResponseDTO director;
    private LocalDateTime createdAt;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public StudentActivityResponseDTO getActivity() {
        return activity;
    }
    
    public void setActivity(StudentActivityResponseDTO activity) {
        this.activity = activity;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public UserResponseDTO getDirector() {
        return director;
    }
    
    public void setDirector(UserResponseDTO director) {
        this.director = director;
    }
}
