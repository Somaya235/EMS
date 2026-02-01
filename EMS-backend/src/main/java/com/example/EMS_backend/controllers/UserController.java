package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.UserProfileWithActivitiesDTO;
import com.example.EMS_backend.dto.StudentActivityResponseDTO;
import com.example.EMS_backend.exceptions.UserNotFoundException;
import com.example.EMS_backend.mappers.StudentActivityMapper;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.services.UserService;
import com.example.EMS_backend.repositories.UserRepository;
import com.example.EMS_backend.repositories.StudentActivityRepository;
import com.example.EMS_backend.repositories.ActivityDirectorRepository;
import com.example.EMS_backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentActivityRepository studentActivityRepository;

    @Autowired
    private ActivityDirectorRepository activityDirectorRepository;

    @Autowired
    private StudentActivityMapper studentActivityMapper;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/{email}")
    public java.util.Optional<User> getByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileWithActivitiesDTO> getUserProfile(@PathVariable Long id) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new UserNotFoundException(id);
        }
        
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        
        // Check if the requested ID matches the current user's ID
        if (!currentUser.getId().equals(id)) {
            throw new UserNotFoundException(id);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Ensure this user is enabled
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new UserNotFoundException(id);
        }

        // Find activities where user is president
        List<StudentActivity> activitiesAsPresident = studentActivityRepository.findByPresident(user);
        List<StudentActivityResponseDTO> presidentActivitiesDTO = studentActivityMapper.toResponseDTOList(activitiesAsPresident);

        // Find activities where user is director
        List<ActivityDirector> activityDirectorships = activityDirectorRepository.findByDirector(user);
        List<StudentActivityResponseDTO> directorActivitiesDTO = activityDirectorships.stream()
                .map(ActivityDirector::getActivity)
                .map(studentActivityMapper::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());

        // Combine all activities into a single list
        List<StudentActivityResponseDTO> allActivities = new java.util.ArrayList<>();
        allActivities.addAll(presidentActivitiesDTO);
        allActivities.addAll(directorActivitiesDTO);

        // Build response with all user data
        UserProfileWithActivitiesDTO responseDTO = new UserProfileWithActivitiesDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getEmail()); // Using email as username since username field doesn't exist
        responseDTO.setEmail(user.getEmail());
        responseDTO.setFirstName(user.getFullName()); // Using fullName as firstName since separate fields don't exist
        responseDTO.setLastName(""); // Empty since lastName field doesn't exist
        responseDTO.setEnabled(user.getEnabled());
        responseDTO.setGrade(user.getGrade());
        responseDTO.setPhoneNumber(user.getPhoneNumber());
        responseDTO.setNationalNumber(user.getNationalNumber());
        responseDTO.setDateOfBirth(user.getDateOfBirth());
        responseDTO.setCvAttachment(user.getCvAttachment());
        responseDTO.setProfileImage(user.getProfileImage());
        responseDTO.setCollageId(user.getCollageId());
        responseDTO.setActivities(allActivities);

        return ResponseEntity.ok(responseDTO);
    }
}
