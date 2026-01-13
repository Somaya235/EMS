package com.example.EMS_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EMS_backend.models.Poll;
import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.Vote;
import com.example.EMS_backend.services.PollOptionService;
import com.example.EMS_backend.services.PollService;
import com.example.EMS_backend.services.UserService;
import com.example.EMS_backend.services.VoteService;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    @Autowired
    private PollOptionService pollOptionService;

    @PostMapping
    public Vote vote(@RequestParam String email,
            @RequestParam Long pollId,
            @RequestParam Long optionId) {

        User user = userService.findByEmail(email);
        Poll poll = pollService.findById(pollId);
        PollOption option = pollOptionService.findById(optionId);

        return voteService.castVote(user, poll, option);
    }
}
