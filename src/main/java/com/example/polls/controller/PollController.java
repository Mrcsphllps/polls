package com.example.polls.controller;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import com.example.polls.repository.ChoiceRepository;
import com.example.polls.repository.PollRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollRepository pollRepository;
    private final ChoiceRepository choiceRepository;

    public PollController(PollRepository pollRepository, ChoiceRepository choiceRepository) {
        this.pollRepository = pollRepository;
        this.choiceRepository = choiceRepository;
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollRepository.save(poll);
    }

    @PostMapping("/{pollId}/vote/{choiceId}")
    public Choice vote(@PathVariable Long pollId, @PathVariable Long choiceId) {

        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Choice not found"));

        choice.setVoteCount(choice.getVoteCount() + 1);

        return choiceRepository.save(choice);
    }
}