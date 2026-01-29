package com.example.EMS_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Response DTO for user data.
 * Excludes sensitive information like password hash.
 */
public class UserResponseDTO {
    
    private Long id;
    private String fullName;
    private String email;
    private Boolean enabled;
    private String grade;
    /**
     * Academic major for the student.
     * Currently mapped from the underlying grade field until a dedicated column exists.
     */
    private String major;
    private String phoneNumber;
    private String nationalNumber;
    private LocalDate dateOfBirth;
    /**
     * Path or URL to the student's CV attachment.
     */
    private String cvAttachment;
    private String profileImage;
    private String collageId;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getNationalNumber() {
        return nationalNumber;
    }
    
    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getCvAttachment() {
        return cvAttachment;
    }
    
    public void setCvAttachment(String cvAttachment) {
        this.cvAttachment = cvAttachment;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public String getCollageId() {
        return collageId;
    }
    
    public void setCollageId(String collageId) {
        this.collageId = collageId;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
