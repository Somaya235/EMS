package com.example.EMS_backend.dto;

import java.time.LocalDate;
import java.util.List;

public class UserProfileWithActivitiesDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private String grade;
    private String phoneNumber;
    private String nationalNumber;
    private LocalDate dateOfBirth;
    private String cvAttachment;
    private String profileImage;
    private String collageId;
    private List<StudentActivityResponseDTO> activities;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List<StudentActivityResponseDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<StudentActivityResponseDTO> activities) {
        this.activities = activities;
    }
}
