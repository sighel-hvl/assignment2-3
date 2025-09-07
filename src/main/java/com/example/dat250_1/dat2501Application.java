package com.example.dat250_1;

import com.example.dat250_1.controller.PollManager;
import com.example.dat250_1.model.Poll;
import com.example.dat250_1.model.User;
import com.example.dat250_1.model.Vote;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@SpringBootApplication
@RestController
public class dat2501Application {

	private final PollManager pollManager = new PollManager();

	public static void main(String[] args) {
		SpringApplication.run(dat2501Application.class, args);
	}

	@PostMapping("/api/createUser")
	public User createUser(@RequestBody UserStructure userStruct) {
		return pollManager.createUser(userStruct.username, userStruct.email);
	}

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable int id) {
        return pollManager.getUserById(id);
    }

    @GetMapping("/api/getUsers")
    public ArrayList<User> getUsers() {
        return pollManager.getAllUsers();
    }

    @PostMapping("/api/createPoll")
    public Poll createPoll(@RequestBody PollStructure poll) {
        return pollManager.createPoll(poll.question,  poll.validUntil, poll.userId, poll.voteOptions);
    }

    @GetMapping("/api/getPolls")
    public ArrayList<Poll> getPolls() {
        return pollManager.getAllPolls();
    }

    @GetMapping("/api/getPollsByUser/{id}")
    public Poll getPollById(@PathVariable int id) {
        return pollManager.getPollsById(id);
    }

    @PostMapping("/api/vote")
    public Vote createVote(@RequestBody VoteStructure voteStruct) {
        return pollManager.createVote(voteStruct.userId, voteStruct.voteOption);
    }

    @PostMapping("/api/changeVote/{id}")
    public Vote changeVote(@PathVariable int id, @RequestBody VoteStructure voteStruct) {
        return pollManager.changeVote(id, voteStruct.voteOption);
    }

    @GetMapping("/api/getvotes/{id}")
    public ArrayList<Vote> getVotesById(@PathVariable int id) {
        return pollManager.getVotesById(id);
    }

    @GetMapping("/api/deletePoll/{id}")
    public void deletePoll(@PathVariable int id) {
        pollManager.deleteVote(id);
    }


}


