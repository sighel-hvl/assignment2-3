package com.example.dat250_1.controller;

import com.example.dat250_1.model.Poll;
import com.example.dat250_1.model.User;
import com.example.dat250_1.model.Vote;
import com.example.dat250_1.model.VoteOption;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class PollManager {
    HashMap<Integer, User> userMap = new HashMap<>();
    HashMap<Integer, Poll> pollMap = new HashMap<>();
    HashMap<Integer, Vote> voteMap = new HashMap<>();
    HashMap<Integer, VoteOption> voteOptionMap = new HashMap<>();
    HashMap<User, HashMap<Vote, Integer>> userVoteMap = new HashMap<>();
    Integer maxUserId = 0;
    Integer maxPollId = 0;
    Integer maxVoteId = 0;
    Integer maxVoteOptionId = 0;
    private final HashMap<Integer, ArrayList<Integer>> pollOptionIds = new HashMap<>();


    public PollManager() {
    }

    public User createUser(String username, String email) {
        maxUserId++;
        User curr = new User(maxUserId, username, email);
        userMap.put(curr.getId(), curr);
        return curr;
    }

    public Poll createPoll(String question, Integer activeHours, Integer userId, ArrayList<String> pollOptions) {
        maxPollId++;
        Poll curr = new Poll(maxPollId, question, Instant.now(), Instant.now().plus(Duration.ofHours(activeHours)), pollOptions, userId);
        pollMap.put(maxPollId, curr);
        userMap.get(userId).getPollIds().add(maxPollId);
        ArrayList<Integer> optionIds = new ArrayList<>();
        int count = 0;
        for (String option : pollOptions) {
            maxVoteOptionId++;
            voteOptionMap.put(maxVoteOptionId, new VoteOption(maxVoteOptionId, option, count));
            optionIds.add(maxVoteOptionId);
            count++;
        }
        pollOptionIds.put(maxPollId, optionIds);
        return curr;
    }

    public ArrayList<VoteOption> getOptionsForPoll(Integer pollId) {
        ArrayList<VoteOption> list = new ArrayList<>();
        ArrayList<Integer> ids = pollOptionIds.get(pollId);
        if (ids != null) {
            for (Integer id : ids) {
                VoteOption vo = voteOptionMap.get(id);
                if (vo != null) list.add(vo);
            }
        }
        return list;
    }

    public Vote createVote(Integer userId, Integer voteOptionId) {
        maxVoteId++;
        Vote curr = new Vote(maxVoteId, Instant.now(), voteOptionId, userId);
        voteMap.put(maxVoteId, curr);
        voteOptionMap.get(voteOptionId).getVoteIds().add(maxVoteId);
        User user = userMap.get(userId);
        user.getVoteIds().add(voteOptionId);
        HashMap<Vote, Integer> votesByUser = userVoteMap.computeIfAbsent(user, u -> new HashMap<>());
        votesByUser.merge(curr, 1, Integer::sum);
        return curr;
    }

    public User getUserById(int id) {
        return userMap.get(id);
    }

    public User getUserByEmail(String email) {
        for(User user : userMap.values()){
            if(user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public ArrayList<Poll> getAllPolls() {
        return new ArrayList<>(pollMap.values());
    }

    public Poll getPollsById(int userId) {
        return pollMap.get(userId);
    }

    public ArrayList<Vote> getVotesById(Integer userId) {
        ArrayList<Vote> votes = new ArrayList<>();
        for (Integer voteId : userMap.get(userId).getVoteIds()) {
            votes.add(voteMap.get(voteId));
        }
        return votes;
    }

    public Vote changeVote(Integer voteId, Integer voteOptionId) {
        voteMap.get(voteId).setvoteOptionId(voteOptionId);
        return voteMap.get(voteId);
    }

    public void deleteVote(Integer pollId) {
        Poll poll = pollMap.get(pollId);
        if (poll != null) {
            List<String> options = poll.getVoteOptionList();
            for (String option : options) {
                for(Vote vote : voteMap.values()){
                    if(voteOptionMap.get(vote.getVoteOptionId()).getCaption().equals(option)){
                        voteMap.remove(vote.getId());
                    }
                }
            }
            pollMap.remove(pollId);
        }
        else{
        }
    }
}