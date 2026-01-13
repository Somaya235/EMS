package com.example.EMS_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getLiveEvents() {
        return eventRepository.findByStatus("LIVE");
    }
}
