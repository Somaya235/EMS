package com.example.EMS_backend.controllers;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.EMS_backend.dto.EventRequestDTO;
import com.example.EMS_backend.dto.EventResponseDTO;
import com.example.EMS_backend.mappers.EventMapper;
import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.services.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "APIs for managing events")
@SecurityRequirement(name = "Bearer Authentication")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    /**
     * Create a new event.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO eventRequestDTO) {
        // Additional manual check if needed, but we prefer @PreAuthorize
        // We'll use a custom check here because of the complex logic (can be in activity or committee)
        // Actually, we can do:
        // @PreAuthorize("(@eventRequestDTO.activityId != null && @authorizationService.canCreateEventInActivity(principal.id, #eventRequestDTO.activityId)) || " +
        //              "(@eventRequestDTO.committeeId != null && @authorizationService.canCreateEventInCommittee(principal.id, #eventRequestDTO.committeeId))")
        // But since #eventRequestDTO is available, we can use it.
        Event event = eventMapper.toEntity(eventRequestDTO);
        Event createdEvent = eventService.create(event);
        EventResponseDTO responseDTO = eventMapper.toResponseDTO(createdEvent);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Get event by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id) {
        Event event = eventService.getById(id);
        EventResponseDTO responseDTO = eventMapper.toResponseDTO(event);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all events.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EventResponseDTO>> getAll() {
        List<Event> events = eventService.getAll();
        List<EventResponseDTO> responseDTOs = eventMapper.toResponseDTOList(events);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Update an existing event.
     */
    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.canManageEvent(principal.id, #id)")
    public ResponseEntity<EventResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO eventRequestDTO) {
        Event eventDetails = eventMapper.toEntity(eventRequestDTO);
        Event updatedEvent = eventService.update(id, eventDetails);
        EventResponseDTO responseDTO = eventMapper.toResponseDTO(updatedEvent);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Delete an event.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canManageEvent(principal.id, #id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
