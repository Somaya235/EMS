package com.example.EMS_backend.services;
import com.example.EMS_backend.repositories.PollOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.EMS_backend.repositories.pollOptionRepository;
import com.example.EMS_backend.models.PollOption;

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
