package com.example.EMS_backend.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.repositories.PollOptionRepository;

@Service
public class PollOptionService {

    @Autowired
    private PollOptionRepository pollOptionRepository;

    public PollOption createOption(PollOption option) {
        return pollOptionRepository.save(option);
    }

    public List<PollOption> getOptionsByPoll(Long pollId) {
        return pollOptionRepository.findByPollId(pollId);
    }

    public PollOption findById(Long id) {
        return pollOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));
    }

    public void deleteOption(Long id) {
        pollOptionRepository.deleteById(id);
    }
}
