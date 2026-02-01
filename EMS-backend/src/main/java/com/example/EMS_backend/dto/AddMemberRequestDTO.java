package com.example.EMS_backend.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding a member to a committee.
 */
public class AddMemberRequestDTO {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    public AddMemberRequestDTO() {}
    
    public AddMemberRequestDTO(Long studentId) {
        this.studentId = studentId;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
