package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.StudentActivityCreationResponseDTO;
import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.dto.StudentActivityResponseDTO;
import com.example.EMS_backend.mappers.StudentActivityMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Admin endpoints for managing student activities.
 *
 * Only users with ROLE_SUPER_ADMIN can access these endpoints.
 */
@RestController
@RequestMapping("/api/admin/student-activities")
@Tag(name = "Admin Student Activities", description = "Admin APIs for managing student activities")
@SecurityRequirement(name = "Bearer Authentication")
public class StudentActivityAdminController {

    @Autowired
    private StudentActivityService studentActivityService;

    @Autowired
    private StudentActivityMapper studentActivityMapper;

    /**
     * Create a new student activity and assign its president.
     *
     * POST /api/admin/student-activities
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create student activity with president", description = "Create a new student activity and assign its president. Only super admins can perform this operation.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created activity and assigned president"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only super admins can create activities")
    })
    public ResponseEntity<StudentActivityCreationResponseDTO> createActivityWithPresident(
            @Parameter(description = "Student activity creation details") @Valid @RequestBody StudentActivityRequestDTO requestDTO) {

        StudentActivity activity = studentActivityService.createActivityWithPresident(requestDTO);

        StudentActivityCreationResponseDTO response =
                new StudentActivityCreationResponseDTO(
                        "SUCCESS",
                        "Student activity created and president assigned successfully",
                        activity.getId(),
                        activity.getPresident().getId()
                );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


  /**
   * Update student activity.
   * Only the activity super admin can update their activity.
   *
   * PUT /student-activities/{id}
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update student activity", description = "Update an existing student activity. Only super admins can perform this operation.")
  @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated activity"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only super admins can update activities"),
        @ApiResponse(responseCode = "404", description = "Activity not found")
    })
  public ResponseEntity<StudentActivityResponseDTO> updateStudentActivity(
    @Parameter(description = "ID of the student activity to update") @PathVariable Long id,
    @Parameter(description = "Updated student activity details") @Valid @RequestBody StudentActivityRequestDTO requestDTO) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long currentUserId = userDetails.getId();

    StudentActivity updatedActivity = studentActivityService.updateActivity(id, requestDTO, currentUserId);
    StudentActivityResponseDTO responseDTO = studentActivityMapper.toResponseDTO(updatedActivity);
    return ResponseEntity.ok(responseDTO);
  }
}

