package com.example.polls.controller;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import com.example.polls.repository.ChoiceRepository;
import com.example.polls.repository.PollRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")

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

    @PutMapping("/{pollId}")
    public Poll updatePoll(@PathVariable Long pollId,
                           @RequestBody Poll updatedPoll) {

        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        poll.setQuestion(updatedPoll.getQuestion());

        for (int i = 0; i < poll.getOptions().size(); i++) {

            poll.getOptions().get(i)
                    .setText(updatedPoll.getOptions().get(i).getText());
        }

        return pollRepository.save(poll);
    }

    @DeleteMapping("/{pollId}")
    public String deletePoll(@PathVariable Long pollId) {
        pollRepository.deleteById(pollId);
        return "Poll deleted successfully";
    }


}