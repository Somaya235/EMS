package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.AddMemberRequestDTO;
import com.example.EMS_backend.dto.CommitteeRequestDTO;
import com.example.EMS_backend.dto.CommitteeResponseDTO;
import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.dto.MessageResponse;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.security.UserDetailsImpl;
import com.example.EMS_backend.services.CommitteeService;
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

@RestController
@RequestMapping("/api/committees")
@Tag(name = "Committees", description = "APIs for managing committees and committee members")
@SecurityRequirement(name = "Bearer Authentication")
public class CommitteeController {

    @Autowired
    private CommitteeService committeeService;

    /**
     * Create a new committee.
     * Only the president of the activity can create committees.
     *
     * POST /api/committees
     */
    @PostMapping
    @PreAuthorize("@authorizationService.canManageActivity(principal.id, #requestDTO.activityId)")
    @Operation(summary = "Create committee", description = "Create a new committee. Only activity presidents can create committees.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created committee"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can create committees")
    })
    public ResponseEntity<CommitteeResponseDTO> createCommittee(
            @Parameter(description = "Committee creation details") @Valid @RequestBody CommitteeRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        CommitteeResponseDTO responseDTO = committeeService.createCommittee(requestDTO, currentUserId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Assign a committee head.
     * Only the president of the activity can assign committee heads.
     *
     * PUT /api/committees/{committeeId}/head
     */
    @PutMapping("/{committeeId}/head")
    @PreAuthorize("@authorizationService.canManageCommittee(principal.id, #committeeId)")
    @Operation(summary = "Assign committee head", description = "Assign a committee head. Only activity presidents can assign committee heads.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully assigned committee head"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can assign committee heads"),
        @ApiResponse(responseCode = "404", description = "Committee not found")
    })
    public ResponseEntity<MessageResponse> assignCommitteeHead(
            @Parameter(description = "ID of the committee") @PathVariable Long committeeId,
            @Parameter(description = "User ID of the committee head") @RequestBody Long headUserId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        committeeService.assignCommitteeHead(committeeId, headUserId, currentUserId);
        return ResponseEntity.ok(new MessageResponse("Committee head assigned successfully"));
    }

    /**
     * Assign a committee director.
     * Only the president of the activity can assign committee directors.
     *
     * PUT /api/committees/{committeeId}/director
     */
    @PutMapping("/{committeeId}/director")
    @PreAuthorize("@authorizationService.canManageCommittee(principal.id, #committeeId)")
    @Operation(summary = "Assign committee director", description = "Assign a committee director. Only activity presidents can assign committee directors.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully assigned committee director"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can assign committee directors"),
        @ApiResponse(responseCode = "404", description = "Committee not found")
    })
    public ResponseEntity<CommitteeResponseDTO> assignCommitteeDirector(
            @Parameter(description = "ID of the committee") @PathVariable Long committeeId,
            @Parameter(description = "User ID of the committee director") @RequestBody Long directorUserId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        CommitteeResponseDTO responseDTO = committeeService.assignCommitteeDirector(committeeId, directorUserId, currentUserId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all committees for a specific activity.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/activity/{activityId}
     */
    @GetMapping("/activity/{activityId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommitteeResponseDTO>> getCommitteesByActivity(@PathVariable Long activityId) {
        List<CommitteeResponseDTO> committees = committeeService.getCommitteesByActivity(activityId);
        return ResponseEntity.ok(committees);
    }

    /**
     * Get a committee by ID.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommitteeResponseDTO> getCommitteeById(@PathVariable Long id) {
        CommitteeResponseDTO committee = committeeService.getCommitteeById(id);
        return ResponseEntity.ok(committee);
    }

    /**
     * Get all committees managed by a specific director.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/director/{directorId}
     */
    @GetMapping("/director/{directorId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommitteeResponseDTO>> getCommitteesByDirector(@PathVariable Long directorId) {
        List<CommitteeResponseDTO> committees = committeeService.getCommitteesByDirector(directorId);
        return ResponseEntity.ok(committees);
    }

    /**
     * Get all members of a specific committee.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/{committeeId}/members
     */
    @GetMapping("/{committeeId}/members")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponseDTO>> getCommitteeMembers(@PathVariable Long committeeId) {
        List<UserResponseDTO> members = committeeService.getCommitteeMembers(committeeId);
        return ResponseEntity.ok(members);
    }

    /**
     * Count members in a specific committee.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/{committeeId}/members/count
     */
    @GetMapping("/{committeeId}/members/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> countCommitteeMembers(@PathVariable Long committeeId) {
        int memberCount = committeeService.countCommitteeMembers(committeeId);
        return ResponseEntity.ok(memberCount);
    }

    /**
     * Count committees in a specific student activity.
     * Accessible to all authenticated users.
     *
     * GET /api/committees/activity/{activityId}/count
     */
    @GetMapping("/activity/{activityId}/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> countCommitteesInActivity(@PathVariable Long activityId) {
        int committeeCount = committeeService.countCommitteesInActivity(activityId);
        return ResponseEntity.ok(committeeCount);
    }

    /**
     * Add a member to a committee.
     * Only the committee head can add members.
     *
     * POST /api/committees/{committeeId}/members
     */
    @PostMapping("/{committeeId}/members")
    @PreAuthorize("@authorizationService.canManageCommittee(principal.id, #committeeId)")
    public ResponseEntity<MessageResponse> addMember(
            @PathVariable Long committeeId,
            @Valid @RequestBody AddMemberRequestDTO requestDTO) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        committeeService.addMember(committeeId, requestDTO, currentUserId);
        return ResponseEntity.ok(new MessageResponse("Member added to committee successfully"));
    }

    /**
     * Search students by name or ID.
     * Accessible to committee heads for adding members.
     *
     * GET /api/committees/search-students?query={searchTerm}
     */
    @GetMapping("/search-students")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponseDTO>> searchStudents(@RequestParam String query) {
        List<User> students = committeeService.searchStudents(query);
        List<UserResponseDTO> studentDTOs = students.stream()
                .map(student -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(student.getId());
                    dto.setFullName(student.getFullName());
                    dto.setEmail(student.getEmail());
                    dto.setCollageId(student.getCollageId());
                    dto.setGrade(student.getGrade());
                    dto.setMajor(student.getMajor());
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }

    /**
     * Get all committees for the current president.
     * Returns all committees where the current user is the president of the associated activity.
     *
     * GET /api/committees/president
     */
    @GetMapping("/president")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommitteeResponseDTO>> getPresidentCommittees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        List<CommitteeResponseDTO> committees = committeeService.getPresidentCommittees(currentUserId);
        return ResponseEntity.ok(committees);
    }

    /**
     * Get all students.
     * Accessible to activity presidents for assigning committee heads and directors.
     *
     * GET /api/committees/all-students
     */
    @GetMapping("/all-students")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        List<User> students = committeeService.getAllStudents();
        List<UserResponseDTO> studentDTOs = students.stream()
                .map(student -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(student.getId());
                    dto.setFullName(student.getFullName());
                    dto.setEmail(student.getEmail());
                    dto.setCollageId(student.getCollageId());
                    dto.setGrade(student.getGrade());
                    dto.setMajor(student.getMajor());
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }

    /**
     * Update a committee.
     * Only the president of the activity can update committees.
     *
     * PUT /api/committees/{committeeId}
     */
    @PutMapping("/{committeeId}")
    @PreAuthorize("@authorizationService.canManageCommittee(principal.id, #committeeId)")
    @Operation(summary = "Update committee", description = "Update committee details. Only activity presidents can update committees.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated committee"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only activity presidents can update committees"),
        @ApiResponse(responseCode = "404", description = "Committee not found")
    })
    public ResponseEntity<CommitteeResponseDTO> updateCommittee(
            @Parameter(description = "ID of the committee") @PathVariable Long committeeId,
            @Parameter(description = "Committee update details") @Valid @RequestBody CommitteeRequestDTO requestDTO) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        CommitteeResponseDTO responseDTO = committeeService.updateCommittee(committeeId, requestDTO, currentUserId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Remove a member from a committee.
     * Only the president of the activity can remove members from committees.
     *
     * DELETE /api/committees/{committeeId}/members/{memberId}
     */
    @DeleteMapping("/{committeeId}/members/{memberId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove member from committee", description = "Remove a member from a committee. Only the committee head can remove members from their own committee.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully removed member from committee"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Only committee heads can remove members from their own committee"),
        @ApiResponse(responseCode = "404", description = "Committee or member not found")
    })
    public ResponseEntity<MessageResponse> removeMemberFromCommittee(
            @Parameter(description = "ID of the committee") @PathVariable Long committeeId,
            @Parameter(description = "ID of the member to remove") @PathVariable Long memberId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        committeeService.removeMemberFromCommittee(committeeId, memberId, currentUserId);
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(new MessageResponse("Member removed from committee successfully"));
    }
}
