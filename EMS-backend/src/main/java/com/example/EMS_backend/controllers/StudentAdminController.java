package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.exceptions.UserNotFoundException;
import com.example.EMS_backend.mappers.UserMapper;
import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin endpoints for viewing student profiles.
 *
 * Only users with the super_admin role can access these endpoints.
 * Endpoints are read-only by design.
 */
@RestController
@RequestMapping("/admin/students")
public class StudentAdminController {

    private static final String STUDENT_ROLE_NAME = "member";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Get a list of all student users.
     *
     * GET /admin/students
     */
    @GetMapping
    @PreAuthorize("hasAuthority('super_admin')")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        List<User> allUsers = userRepository.findAll();

        List<User> students = allUsers.stream()
                // Only enabled users
                .filter(user -> Boolean.TRUE.equals(user.getEnabled()))
                // Only users that have the "member" role (treated as students)
                .filter(this::hasStudentRole)
                .collect(Collectors.toList());

        List<UserResponseDTO> response = userMapper.toResponseDTOList(students);
        return ResponseEntity.ok(response);
    }

    /**
     * Get full profile details for a specific student.
     *
     * GET /admin/students/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('super_admin')")
    public ResponseEntity<UserResponseDTO> getStudentProfile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Ensure this user is an enabled student
        if (!Boolean.TRUE.equals(user.getEnabled()) || !hasStudentRole(user)) {
            throw new UserNotFoundException(id);
        }

        UserResponseDTO responseDTO = userMapper.toResponseDTO(user);
        return ResponseEntity.ok(responseDTO);
    }

    private boolean hasStudentRole(User user) {
        if (user.getRoles() == null) {
            return false;
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(STUDENT_ROLE_NAME::equals);
    }
}

