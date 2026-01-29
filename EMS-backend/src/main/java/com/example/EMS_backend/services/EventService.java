package com.example.EMS_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EMS_backend.exceptions.ResourceNotFoundException;
import com.example.EMS_backend.exceptions.BadRequestException;
import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Create a new event.
     */
    public Event create(Event event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new BadRequestException("Event title is required");
        }
        if (event.getStartAt() == null || event.getEndAt() == null) {
            throw new BadRequestException("Event start and end times are required");
        }
        if (event.getEndAt().isBefore(event.getStartAt())) {
            throw new BadRequestException("Event end time must be after start time");
        }
        return eventRepository.save(event);
    }

    /**
     * Get event by ID.
     */
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
    }

    /**
     * Get all events.
     */
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    /**
     * Update an existing event.
     */
    public Event update(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));

        if (eventDetails.getTitle() != null) {
            event.setTitle(eventDetails.getTitle());
        }
        if (eventDetails.getDescription() != null) {
            event.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getStartAt() != null) {
            event.setStartAt(eventDetails.getStartAt());
        }
        if (eventDetails.getEndAt() != null) {
            event.setEndAt(eventDetails.getEndAt());
        }
        if (eventDetails.getLocation() != null) {
            event.setLocation(eventDetails.getLocation());
        }
        if (eventDetails.getStatus() != null) {
            event.setStatus(eventDetails.getStatus());
        }
        if (eventDetails.getTags() != null) {
            event.setTags(eventDetails.getTags());
        }
        if (eventDetails.getBannerImage() != null) {
            event.setBannerImage(eventDetails.getBannerImage());
        }
        if (eventDetails.getEventColor() != null) {
            event.setEventColor(eventDetails.getEventColor());
        }

        return eventRepository.save(event);
    }

    /**
     * Delete an event.
     */
    public void delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id));
        eventRepository.delete(event);
    }
}
