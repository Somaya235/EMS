package com.example.EMS_backend.controllers;

import com.example.EMS_backend.dto.MessageResponse;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('member') or hasRole('committee_member') or hasRole('committee_head') or hasRole('activity_director') or hasRole('web_manager') or hasRole('activity_president') or hasRole('super_admin')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/member")
    @PreAuthorize("hasRole('member')")
    public String memberAccess() {
        return "Member Board.";
    }

    @GetMapping("/committee-member")
    @PreAuthorize("hasRole('committee_member')")
    public String committeeMemberAccess() {
        return "Committee Member Board.";
    }

    @GetMapping("/committee-head")
    @PreAuthorize("hasRole('committee_head')")
    public String committeeHeadAccess() {
        return "Committee Head Board.";
    }

    @GetMapping("/activity-director")
    @PreAuthorize("hasRole('activity_director')")
    public String activityDirectorAccess() {
        return "Activity Director Board.";
    }

    @GetMapping("/web-manager")
    @PreAuthorize("hasRole('web_manager')")
    public String webManagerAccess() {
        return "Web Manager Board.";
    }

    @GetMapping("/activity-president")
    @PreAuthorize("hasRole('activity_president')")
    public String activityPresidentAccess() {
        return "Activity President Board.";
    }

    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('super_admin')")
    public String superAdminAccess() {
        return "Super Admin Board.";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('super_admin') or hasRole('web_manager')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
