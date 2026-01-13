package com.example.EMS_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.models.Poll;
import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.models.Vote;
import com.example.EMS_backend.repositories.EventRepository;
import com.example.EMS_backend.repositories.VoteRepository;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public Vote castVote(User user, Poll poll, PollOption option) {

        // Prevent double voting
        if (voteRepository.existsByUserIdAndPollId(
                user.getId(),
                poll.getId())) {
            throw new RuntimeException("User already voted in this poll");
        }

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPoll(poll);
        vote.setOption(option);

        return voteRepository.save(vote);
    }
}
