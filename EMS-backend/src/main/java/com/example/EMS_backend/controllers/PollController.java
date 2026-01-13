package com.example.EMS_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EMS_backend.models.Poll;
import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.Vote;
import com.example.EMS_backend.services.PollService;
import com.example.EMS_backend.services.VoteService;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @GetMapping("/event/{eventId}")
    public List<Poll> getByEvent(@PathVariable Long eventId) {
        return pollService.getPollsByEvent(eventId);
    }
}
