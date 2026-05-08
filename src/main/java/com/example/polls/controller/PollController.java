package com.example.polls.controller;

import com.example.polls.model.Poll;
import com.example.polls.repository.PollRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollRepository pollRepository;

    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollRepository.save(poll);
    }
}