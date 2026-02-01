package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.dto.StudentActivityResponseDTO;
import com.example.EMS_backend.dto.ActivityDirectorRequestDTO;
import com.example.EMS_backend.dto.ActivityDirectorResponseDTO;
import com.example.EMS_backend.mappers.ActivityDirectorMapper;
import com.example.EMS_backend.mappers.StudentActivityMapper;
import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.security.UserDetailsImpl;
import com.example.EMS_backend.services.StudentActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public and president-specific endpoints for student activities.
 */
@RestController
@RequestMapping("/student-activities")
public class StudentActivityController {

    @Autowired
    private StudentActivityService studentActivityService;

    @Autowired
    private StudentActivityMapper studentActivityMapper;

    @Autowired
    private ActivityDirectorMapper activityDirectorMapper;

    /**
     * Get all student activities.
     * Accessible to all authenticated users.
     *
     * GET /student-activities
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<StudentActivityResponseDTO>> getAllStudentActivities() {
        List<StudentActivity> activities = studentActivityService.getAllActivities();
        List<StudentActivityResponseDTO> responseDTOs = studentActivityMapper.toResponseDTOList(activities);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get student activity by ID.
     * Accessible to all authenticated users.
     *
     * GET /student-activities/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StudentActivityResponseDTO> getStudentActivityById(@PathVariable Long id) {
        StudentActivity activity = studentActivityService.getActivityById(id);
        StudentActivityResponseDTO responseDTO = studentActivityMapper.toResponseDTO(activity);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Count all student activities.
     * Accessible to all authenticated users.
     *
     * GET /student-activities/count
     */
    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> countStudentActivities() {
        long count = studentActivityService.countActivities();
        return ResponseEntity.ok(count);
    }

    /**
     * Assign or update an Activity Director for a specific Student Activity.
     * Only the president of the activity can perform this operation.
     *
     * PUT /student-activities/{activityId}/directors
     */
    @PutMapping("/{activityId}/directors")
    @PreAuthorize("hasAuthority('activity_president')")
    public ResponseEntity<ActivityDirectorResponseDTO> assignActivityDirector(
            @PathVariable Long activityId,
            @Valid @RequestBody ActivityDirectorRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        ActivityDirector activityDirector = studentActivityService.assignActivityDirector(activityId, requestDTO, currentUserId);
        ActivityDirectorResponseDTO responseDTO = activityDirectorMapper.toResponseDTO(activityDirector);
        return ResponseEntity.ok(responseDTO);
    }


}
