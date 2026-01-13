package com.example.EMS_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.models.Poll;
import com.example.EMS_backend.repositories.EventRepository;
import com.example.EMS_backend.repositories.PollRepository;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    public Poll findById(Long eventId) {
        return (Poll) pollRepository.findById(eventId)
                .map(List::of)
                .orElseGet(List::of);
    }
    public List<Poll> getPollsByEvent(Long eventId) {
        return pollRepository.findByEventId(eventId);
    }
}
