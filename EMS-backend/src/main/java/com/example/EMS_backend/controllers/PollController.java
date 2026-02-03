package com.example.EMS_backend.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.EMS_backend.models.Poll;
import com.example.EMS_backend.services.PollService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/polls")
@Tag(name = "Polls", description = "APIs for managing polls")
@SecurityRequirement(name = "Bearer Authentication")
public class PollController {

    @Autowired
    private PollService pollService;

    @GetMapping("/event/{eventId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get polls by event", description = "Retrieve all polls associated with a specific event.")
    public ResponseEntity<List<Poll>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(pollService.getPollsByEvent(eventId));
    }
}
