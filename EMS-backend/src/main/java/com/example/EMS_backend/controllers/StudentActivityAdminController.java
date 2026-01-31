package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.StudentActivityCreationResponseDTO;
import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.dto.StudentActivityResponseDTO;
import com.example.EMS_backend.mappers.StudentActivityMapper;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.security.UserDetailsImpl;
import com.example.EMS_backend.services.StudentActivityService;
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
@RequestMapping("/admin/student-activities")
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
    @PreAuthorize("hasAuthority('super_admin')")
    public ResponseEntity<StudentActivityCreationResponseDTO> createActivityWithPresident(
            @Valid @RequestBody StudentActivityRequestDTO requestDTO) {

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
  @PreAuthorize("hasAuthority('super_admin')")
  public ResponseEntity<StudentActivityResponseDTO> updateStudentActivity(
    @PathVariable Long id,
    @Valid @RequestBody StudentActivityRequestDTO requestDTO) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long currentUserId = userDetails.getId();

    StudentActivity updatedActivity = studentActivityService.updateActivity(id, requestDTO, currentUserId);
    StudentActivityResponseDTO responseDTO = studentActivityMapper.toResponseDTO(updatedActivity);
    return ResponseEntity.ok(responseDTO);
  }
}

