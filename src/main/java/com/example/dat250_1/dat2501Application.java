package com.example.dat250_1;

import redis.clients.jedis.UnifiedJedis;
import com.example.dat250_1.controller.PollManager;
import com.example.dat250_1.model.Poll;
import com.example.dat250_1.model.User;
import com.example.dat250_1.model.Vote;
import com.example.dat250_1.model.VoteOption;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class dat2501Application {

	private final PollManager pollManager = new PollManager();

	public static void main(String[] args) {
        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");
        System.out.println(jedis.ping());
        //Task 1
        jedis.del("users");
        jedis.sadd("users", "alice");
        jedis.sadd("users", "bob");
        System.out.println(jedis.smembers("users"));
        jedis.srem("users", "alice");
        System.out.println(jedis.smembers("users"));
        jedis.sadd("users", "eve");
        System.out.println(jedis.smembers("users"));

        //Task 2
        jedis.hset("polls:1:info", "id", "1");
        jedis.hset("polls:1:info", "title", "Pinapple on pizza?");
        System.out.println(jedis.hget("polls:1:info", "title"));
        jedis.hset("polls:1:voteoptions", "Yes, yammy", "269");
        jedis.hset("polls:1:voteoptions", "Mamma mia, no!", "268");
        jedis.hset("polls:1:voteoptions", "dont care", "42");
        jedis.hincrBy("polls:1:voteoptions", "dont care", 3);
        System.out.println(jedis.hget("polls:1:voteoptions", "dont care"));
        System.out.println(jedis.hgetAll("polls:1:voteoptions"));
        jedis.close();
		SpringApplication.run(dat2501Application.class, args);
	}

    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping("/api/createUser")
	public User createUser(@RequestBody UserStructure userStruct) {
		return pollManager.createUser(userStruct.username, userStruct.email);
	}

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/getUser/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return pollManager.getUserByEmail(email);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/getUsers")
    public ArrayList<User> getUsers() {
        return pollManager.getAllUsers();
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping("/api/createPoll")
    public Poll createPoll(@RequestBody PollStructure poll) {
        return pollManager.createPoll(poll.question,  poll.validUntil, poll.userId, poll.voteOptions);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/getPolls")
    public ArrayList<Poll> getPolls() {
        return pollManager.getAllPolls();
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/polls/{pollId}/options")
    public ArrayList<VoteOption> getPollOptions(@PathVariable Integer pollId) {
        return pollManager.getOptionsForPoll(pollId);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/getPollsByUser/{id}")
    public Poll getPollById(@PathVariable int id) {
        return pollManager.getPollsById(id);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping("/api/vote")
    public Vote createVote(@RequestBody VoteStructure voteStruct) {
        return pollManager.createVote(voteStruct.userId, voteStruct.voteOption);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @PostMapping("/api/changeVote/{id}")
    public Vote changeVote(@PathVariable int id, @RequestBody VoteStructure voteStruct) {
        return pollManager.changeVote(id, voteStruct.voteOption);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/getvotes/{id}")
    public ArrayList<Vote> getVotesById(@PathVariable int id) {
        return pollManager.getVotesById(id);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/deletePoll/{id}")
    public void deletePoll(@PathVariable int id) {
        pollManager.deleteVote(id);
    }

    @CrossOrigin(origins = "http://localhost:5174")
    @GetMapping("/api/polls/{pollId}/voteCounts")
    public java.util.Map<String, Integer> getPollVoteCounts(@PathVariable Integer pollId) {
        return pollManager.getVoteCount(pollId);
    }



}


