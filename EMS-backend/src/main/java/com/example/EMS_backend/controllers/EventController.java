package com.example.EMS_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.services.EventService;
import com.example.EMS_backend.services.PollService;
import com.example.EMS_backend.services.VoteService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public Event create(@RequestBody Event event) {
        return eventService.create(event);
    }


}
