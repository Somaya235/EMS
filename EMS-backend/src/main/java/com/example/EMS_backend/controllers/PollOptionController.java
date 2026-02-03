package com.example.EMS_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.services.PollOptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/options")
@Tag(name = "Poll Options", description = "APIs for managing poll options")
@SecurityRequirement(name = "Bearer Authentication")
public class PollOptionController {

    @Autowired
    private PollOptionService pollOptionService;

    // create option
    @PostMapping
    @PreAuthorize("@authorizationService.canManagePoll(principal.id, #option.poll.id)")
    @Operation(summary = "Create poll option", description = "Create a new option for a specific poll.")
    public ResponseEntity<PollOption> create(@RequestBody PollOption option) {
        return ResponseEntity.ok(pollOptionService.createOption(option));
    }

    // view options for a poll
    @GetMapping("/poll/{pollId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get options by poll", description = "Retrieve all options associated with a specific poll.")
    public ResponseEntity<List<PollOption>> getByPoll(@PathVariable Long pollId) {
        return ResponseEntity.ok(pollOptionService.getOptionsByPoll(pollId));
    }

    // delete option
    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.isAdmin(principal.id)") // Simple check for now as delete is sensitive
    @Operation(summary = "Delete poll option", description = "Delete a specific poll option.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pollOptionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}
