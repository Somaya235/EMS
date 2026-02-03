package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.mappers.UserMapper;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/admin/students")
public class StudentAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Get a list of all users.
     *
     * GET /admin/students
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllStudents() {
        List<User> allUsers = userRepository.findAll();

        List<User> enabledUsers = allUsers.stream()
                // Only enabled users
                .filter(user -> Boolean.TRUE.equals(user.getEnabled()))
                .collect(Collectors.toList());

        List<UserResponseDTO> response = userMapper.toResponseDTOList(enabledUsers);
        return ResponseEntity.ok(response);
    }
}

