package com.example.EMS_backend.service;

import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.repositories.EventRepository;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Example service demonstrating audit logging usage.
 * Shows how to properly set audit context before database operations.
 */
@Service
@Transactional
public class EventManagementService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    /**
     * Creates a new event with proper audit logging.
     */
    public Event createEvent(Long currentUserId, String title, String description, Long activityId) {
        return auditService.executeWithAudit(currentUserId, () -> {
            // Verify user exists
            User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Create and save event
            Event event = new Event();
            event.setTitle(title);
            event.setDescription(description);
            event.setStartAt(LocalDateTime.now());
            event.setEndAt(LocalDateTime.now().plusHours(2));

            // Set other required fields...

            Event savedEvent = eventRepository.save(event);

            // This INSERT operation will be automatically logged with:
            // - table_name: 'events'
            // - record_id: savedEvent.getId()
            // - action: 'INSERT'
            // - performed_by: currentUserId
            // - new_data: JSON representation of the event
            // - performed_at: current timestamp

            return savedEvent;
        });
    }

    /**
     * Updates an event with proper audit logging.
     */
    public Event updateEvent(Long currentUserId, Long eventId, String newTitle) {
        return auditService.executeWithAudit(currentUserId, () -> {
            Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

            String oldTitle = event.getTitle();
            event.setTitle(newTitle);

            Event updatedEvent = eventRepository.save(event);

            // This UPDATE operation will be automatically logged with:
            // - table_name: 'events'
            // - record_id: eventId
            // - action: 'UPDATE'
            // - performed_by: currentUserId
            // - old_data: JSON with old title
            // - new_data: JSON with new title
            // - performed_at: current timestamp

            return updatedEvent;
        });
    }

    /**
     * Deletes an event with proper audit logging.
     */
    public void deleteEvent(Long currentUserId, Long eventId) {
        auditService.executeWithAudit(currentUserId, () -> {
            Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

            eventRepository.delete(event);

            // This DELETE operation will be automatically logged with:
            // - table_name: 'events'
            // - record_id: eventId
            // - action: 'DELETE'
            // - performed_by: currentUserId
            // - old_data: JSON representation of the deleted event
            // - new_data: null
            // - performed_at: current timestamp

            return null;
        });
    }

    /**
     * Example of manual audit context management (alternative to executeWithAudit).
     */
    public Event createEventManual(Long currentUserId, String title) {
        try {
            // Set audit context manually
            auditService.setCurrentUser(currentUserId);

            Event event = new Event();
            event.setTitle(title);
            event.setStartAt(LocalDateTime.now());

            return eventRepository.save(event);

        } finally {
            // Always clear the context
            auditService.clearCurrentUser();
        }
    }
}
