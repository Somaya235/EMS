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

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    /**
     * Create a new event.
     */
    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO eventRequestDTO) {
        Event event = eventMapper.toEntity(eventRequestDTO);
        Event createdEvent = eventService.create(event);
        EventResponseDTO responseDTO = eventMapper.toResponseDTO(createdEvent);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Get event by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id) {
        Event event = eventService.getById(id);
        EventResponseDTO responseDTO = eventMapper.toResponseDTO(event);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all events.
     */
    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getAll() {
        List<Event> events = eventService.getAll();
        List<EventResponseDTO> responseDTOs = eventMapper.toResponseDTOList(events);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Update an existing event.
     */
    @PutMapping("/{id}")
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
