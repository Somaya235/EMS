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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/student-activities")
@Tag(name = "Student Activities", description = "APIs for managing and viewing student activities")
@SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(summary = "Get all student activities", description = "Retrieve a list of all student activities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of activities"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
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
    @Operation(summary = "Get student activity by ID", description = "Retrieve a specific student activity by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved activity"),
        @ApiResponse(responseCode = "404", description = "Activity not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<StudentActivityResponseDTO> getStudentActivityById(
            @Parameter(description = "ID of the student activity to retrieve") @PathVariable Long id) {
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
    @Operation(summary = "Count all student activities", description = "Get the total number of student activities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved count"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Long> countStudentActivities() {
        long count = studentActivityService.countActivities();
        return ResponseEntity.ok(count);
    }

    /**
     * Get all directors for a specific student activity.
     * Accessible to all authenticated users.
     *
     * GET /student-activities/{activityId}/directors
     */
    @GetMapping("/{activityId}/directors")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all directors for student activity", description = "Retrieve all directors assigned to a specific student activity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved directors"),
        @ApiResponse(responseCode = "404", description = "Activity not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<ActivityDirectorResponseDTO>> getDirectorsByActivity(
            @Parameter(description = "ID of the student activity") @PathVariable Long activityId) {
        List<ActivityDirector> directors = studentActivityService.getDirectorsByActivity(activityId);
        List<ActivityDirectorResponseDTO> responseDTOs = activityDirectorMapper.toResponseDTOList(directors);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Assign or update an Activity Director for a specific Student Activity.
     * Only the president of the activity can perform this operation.
     *
     * PUT /student-activities/{activityId}/directors
     */
    @PutMapping("/{activityId}/directors")
    @PreAuthorize("hasAuthority('activity_president')")
    @Operation(summary = "Assign activity director", description = "Assign or update an Activity Director for a specific Student Activity. Only the president can perform this operation.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully assigned director"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can assign directors"),
        @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    public ResponseEntity<ActivityDirectorResponseDTO> assignActivityDirector(
            @Parameter(description = "ID of the student activity") @PathVariable Long activityId,
            @Parameter(description = "Activity director assignment details") @Valid @RequestBody ActivityDirectorRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        ActivityDirector activityDirector = studentActivityService.assignActivityDirector(activityId, requestDTO, currentUserId);
        ActivityDirectorResponseDTO responseDTO = activityDirectorMapper.toResponseDTO(activityDirector);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Update a student activity.
     * Only the president of the activity can update it.
     *
     * PUT /student-activities/{activityId}
     */
    @PutMapping("/{activityId}")
    @PreAuthorize("hasAuthority('activity_president')")
    @Operation(summary = "Update student activity", description = "Update a student activity. Only the activity president can update it.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated activity"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can update activities"),
        @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    public ResponseEntity<StudentActivityResponseDTO> updateStudentActivity(
            @Parameter(description = "ID of the student activity to update") @PathVariable Long activityId,
            @Parameter(description = "Updated activity details") @Valid @RequestBody StudentActivityRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        StudentActivity updatedActivity = studentActivityService.updateActivity(activityId, requestDTO, currentUserId);
        StudentActivityResponseDTO responseDTO = studentActivityMapper.toResponseDTO(updatedActivity);
        return ResponseEntity.ok(responseDTO);
    }


}
