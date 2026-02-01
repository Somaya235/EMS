package com.example.EMS_backend.services;

import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.dto.ActivityDirectorRequestDTO;
import com.example.EMS_backend.exceptions.*;
import com.example.EMS_backend.mappers.StudentActivityMapper;
import com.example.EMS_backend.mappers.ActivityDirectorMapper;
import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.ActivityDirectorId;
import com.example.EMS_backend.repositories.RoleRepository;
import com.example.EMS_backend.repositories.StudentActivityRepository;
import com.example.EMS_backend.repositories.UserRepository;
import com.example.EMS_backend.repositories.ActivityDirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing student activities and president assignments.
 */
@Service
public class StudentActivityService {

    private static final String ACTIVITY_PRESIDENT_ROLE = "activity_president";
    private static final String ACTIVITY_DIRECTOR_ROLE = "activity_director";

    @Autowired
    private StudentActivityRepository studentActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentActivityMapper studentActivityMapper;

    @Autowired
    private ActivityDirectorRepository activityDirectorRepository;

    @Autowired
    private ActivityDirectorMapper activityDirectorMapper;

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

    /**
     * Get a list of all student activities.
     */
    public List<StudentActivity> getAllActivities() {
        return studentActivityRepository.findAll();
    }

    /**
     * Get a student activity by ID.
     */
    public StudentActivity getActivityById(Long activityId) {
        return studentActivityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));
    }

    /**
     * Update an existing student activity.
     * Only president can update their activity.
     */
    @Transactional
    public StudentActivity updateActivity(Long activityId, StudentActivityRequestDTO request, Long currentUserId) {
        StudentActivity activity = studentActivityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        // Check activity name uniqueness if name is changed
        if (!activity.getName().equals(request.getName()) && studentActivityRepository.existsByName(request.getName())) {
            throw new ActivityAlreadyExistsException(request.getName());
        }

        // Map updated fields from DTO to entity
        studentActivityMapper.updateEntityFromDTO(request, activity);
        return studentActivityRepository.save(activity);
    }

    /**
     * Count all student activities.
     */
    public long countActivities() {
        return studentActivityRepository.count();
    }

    /**
     * Get all directors for a specific student activity.
     */
    public List<ActivityDirector> getDirectorsByActivity(Long activityId) {
        // Verify activity exists
        if (!studentActivityRepository.existsById(activityId)) {
            throw new ActivityNotFoundException(activityId);
        }
        return activityDirectorRepository.findByActivityId(activityId);
    }

    /**
     * Assigns or updates an Activity Director for a specific Student Activity.
     * Only the president of the activity can perform this operation.
     *
     * Business rules:
     * - Student Activity must exist.
     * - Director user must exist.
     * - Director user must have the 'activity_director' role.
     * - Caller must be the president of the specified Student Activity.
     */
    @Transactional
    public ActivityDirector assignActivityDirector(
            Long activityId, ActivityDirectorRequestDTO requestDTO, Long currentUserId) {

        StudentActivity activity = studentActivityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        // Ensure current user is the president of this activity
        if (!activity.getPresident().getId().equals(currentUserId)) {
            throw new UnauthorizedOperationException("You are not authorized to assign/update directors for this activity.");
        }

        User director = userRepository.findById(requestDTO.getDirectorId())
                .orElseThrow(() -> new UserNotFoundException(requestDTO.getDirectorId()));

        // Ensure the assigned director has the 'activity_director' role
        boolean hasDirectorRole = director.getRoles().stream()
                .anyMatch(role -> role.getName().equals(ACTIVITY_DIRECTOR_ROLE));
        if (!hasDirectorRole) {
            throw new RoleNotFoundException("Assigned user does not have the activity_director role.");
        }

        // Check if an ActivityDirector entry already exists for this activity and director
        ActivityDirectorId activityDirectorId = new ActivityDirectorId(activityId, director.getId());
        Optional<ActivityDirector> existingActivityDirector = activityDirectorRepository.findById(activityDirectorId);

        ActivityDirector activityDirector;
        if (existingActivityDirector.isPresent()) {
            activityDirector = existingActivityDirector.get();
            // Update existing entry
            activityDirectorMapper.updateEntityFromDTO(requestDTO, activityDirector);
        } else {
            // Create new entry
            activityDirector = activityDirectorMapper.toEntity(requestDTO, director);
            activityDirector.setActivity(activity);
            activityDirector.setId(activityDirectorId); // Set the composite ID
        }

        return activityDirectorRepository.save(activityDirector);
    }
}

