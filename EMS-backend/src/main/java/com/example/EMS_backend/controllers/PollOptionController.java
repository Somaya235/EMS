package com.example.EMS_backend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.EMS_backend.models.PollOption;
import com.example.EMS_backend.services.PollOptionService;
import java.util.List;

@RestController
@RequestMapping("/api/options")
public class PollOptionController {

    @Autowired
    private PollOptionService pollOptionService;

    // ADMIN: create option
    @PostMapping
    public PollOption create(@RequestBody PollOption option) {
        return pollOptionService.createOption(option);
    }

    // USER: view options for a poll
    @GetMapping("/poll/{pollId}")
    public List<PollOption> getByPoll(@PathVariable Long pollId) {
        return pollOptionService.getOptionsByPoll(pollId);
    }

    // ADMIN: delete option
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pollOptionService.deleteOption(id);
    }
}
