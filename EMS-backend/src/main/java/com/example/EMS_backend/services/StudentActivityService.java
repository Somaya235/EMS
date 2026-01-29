package com.example.EMS_backend.services;

import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.exceptions.ActivityAlreadyExistsException;
import com.example.EMS_backend.exceptions.PresidentAlreadyAssignedException;
import com.example.EMS_backend.exceptions.RoleNotFoundException;
import com.example.EMS_backend.exceptions.UserNotFoundException;
import com.example.EMS_backend.mappers.StudentActivityMapper;
import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.RoleRepository;
import com.example.EMS_backend.repositories.StudentActivityRepository;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing student activities and president assignments.
 */
@Service
public class StudentActivityService {

    private static final String ACTIVITY_PRESIDENT_ROLE = "activity_president";

    @Autowired
    private StudentActivityRepository studentActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentActivityMapper studentActivityMapper;

    /**
     * Create a new student activity and assign a president in a single transaction.
     *
     * Business rules:
     * - Activity name must be unique
     * - President user must exist
     * - President can only be assigned to one activity
     * - President must get the activity_president role
     */
    @Transactional
    public StudentActivity createActivityWithPresident(StudentActivityRequestDTO request) {
        // Check activity name uniqueness
        if (studentActivityRepository.existsByName(request.getName())) {
            throw new ActivityAlreadyExistsException(request.getName());
        }

        // Fetch president user
        User president = userRepository.findById(request.getPresidentId())
                .orElseThrow(() -> new UserNotFoundException(request.getPresidentId()));

        // Ensure president is not already assigned to another activity
        if (studentActivityRepository.existsByPresident(president)) {
            throw new PresidentAlreadyAssignedException(president.getId());
        }

        // Load activity_president role
        Role presidentRole = roleRepository.findByName(ACTIVITY_PRESIDENT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException(ACTIVITY_PRESIDENT_ROLE));

        // Map request to entity
        StudentActivity activity = studentActivityMapper.toEntity(request);
        activity.setCategory(request.getCategory());
        activity.setPresident(president);

        // Persist activity
        StudentActivity savedActivity = studentActivityRepository.save(activity);

        // Grant president role if not already present
        if (!president.getRoles().contains(presidentRole)) {
            president.getRoles().add(presidentRole);
            userRepository.save(president);
        }

        return savedActivity;
    }
}

