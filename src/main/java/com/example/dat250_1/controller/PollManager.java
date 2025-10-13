package com.example.dat250_1.controller;

import com.example.dat250_1.model.Poll;
import com.example.dat250_1.model.User;
import com.example.dat250_1.model.Vote;
import com.example.dat250_1.model.VoteOption;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.UnifiedJedis;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final UnifiedJedis jedis;
    private final UnifiedJedis subscriberJedis;
    private final HashMap<Integer, ArrayList<Integer>> pollOptionIds = new HashMap<>();

    public PollManager() {
        this.jedis = new UnifiedJedis("redis://localhost:6379");
        this.subscriberJedis = new UnifiedJedis("redis://localhost:6379");
        Thread thread = new Thread(() -> {
            subscriberJedis.psubscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    System.out.println(channel + message);
                    handleVoteEvent(channel, message);
                }
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    System.out.println(channel + message);
                    handleVoteEvent(channel, message);
                }
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.out.println(pattern);
                }
                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println(channel + "number of subs: "+ subscribedChannels);
                }
            }, "poll:*:vote");
        });
        thread.start();
    }

    private void handleVoteEvent(String channel, String message) {

            String[] parts = channel.split(":");
            if (parts.length < 2) return;

            Integer pollId = Integer.parseInt(parts[1]);

            String[] msgParts = message.split(":");
            if (msgParts.length < 2) return;

            Integer voteOptionId = Integer.parseInt(msgParts[0]);
            Integer userId = Integer.parseInt(msgParts[1]);
            createVoteFromEvent(userId, voteOptionId, pollId);
    }

    private void createVoteFromEvent(Integer userId, Integer voteOptionId, Integer pollId) {
        maxVoteId++;
        Vote curr = new Vote(maxVoteId, Instant.now(), voteOptionId, userId);
        voteMap.put(maxVoteId, curr);

        voteOptionMap.get(voteOptionId).getVoteIds().add(maxVoteId);

        if (userId != null && userMap.containsKey(userId)) {
            User user = userMap.get(userId);
            user.getVoteIds().add(voteOptionId);
            HashMap<Vote, Integer> votesByUser = userVoteMap.computeIfAbsent(user, u -> new HashMap<>());
            votesByUser.merge(curr, 1, Integer::sum);
        }

        deletePollCache(pollId);

        System.out.println("Vote recorded in database: " + maxVoteId);
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
        String name = "poll:"+maxPollId;
        pollOptionIds.put(maxPollId, optionIds);
        jedis.publish(name, question);
        System.out.println("Subscribed to: "+name + ", Topic: "+ question);
        return curr;
    }

    public void publishVoteEvent(Integer pollId, Integer voteOptionId, Integer userId) {
        String name = "pollId: " + pollId;
        String message = voteOptionId + " by user " + userId;
        jedis.publish(name, message);
        System.out.println("Vote to " + name + " on voteoption " + message);
    }

    public void subscribePoll(Integer pollId, Integer userId) {
        String channelPattern = "poll:" + pollId;
        UnifiedJedis pollSubscriber = new UnifiedJedis("redis://localhost:6379");

        Thread subscriptionThread = new Thread(() -> {
            pollSubscriber.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    System.out.println("User " + userId + " received message on " + channel + ": " + message);
                }
                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    System.out.println("User " + userId + " subscribed to: " + channel);
                }
                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println("User " + userId + " unsubscribed from: " + channel);
                }
            }, channelPattern);
        });

        subscriptionThread.setDaemon(true);
        subscriptionThread.start();
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

        if (userId != null) {
            User user = userMap.get(userId);
            user.getVoteIds().add(voteOptionId);
            HashMap<Vote, Integer> votesByUser = userVoteMap.computeIfAbsent(user, u -> new HashMap<>());
            votesByUser.merge(curr, 1, Integer::sum);
        }

        Integer pollId = pollIdByVoteOption(voteOptionId);
        if(pollId != null) {
            deletePollCache(pollId);
        }

        publishVoteEvent(pollId, voteOptionId, userId);
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
        Vote vote = voteMap.get(voteId);
        Integer oldVoteOptionId = vote.getVoteOptionId();

        Integer oldPollId = pollIdByVoteOption(oldVoteOptionId);
        Integer newPollId = pollIdByVoteOption(voteOptionId);

        vote.setvoteOptionId(voteOptionId);

        if (oldPollId != null) {
            deletePollCache(oldPollId);
        }
        if (newPollId != null && !newPollId.equals(oldPollId)) {
            deletePollCache(newPollId);
        }

        return vote;

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
            deletePollCache(pollId);
        }
    }

    private Map<String, Integer> voteCountsFromDb(Integer pollId) {
        Map<String, Integer> counts = new HashMap<>();

        ArrayList<Integer> optionIds = pollOptionIds.get(pollId);
        if (optionIds == null) {
            return counts;
        }
        for (Integer optionId : optionIds) {
            VoteOption option = voteOptionMap.get(optionId);
            if (option != null) {
                int voteCount = option.getVoteIds().size();
                counts.put(option.getCaption(), voteCount);
            }
        }
        return counts;
    }


    public Map<String, Integer> getVoteCount(Integer pollId) {
        String key = "poll:" + pollId + ":votes";
        if(jedis.exists(key)){
            Map<String, String> cache = jedis.hgetAll(key);
            Map<String, Integer> res = new HashMap<>();
            for (Map.Entry<String, String> entry : cache.entrySet()) {
                res.put(entry.getKey(), Integer.parseInt(entry.getValue()));
            }
            return res;
        }
        Map<String, Integer> voteCounts = voteCountsFromDb(pollId);
        cacheVote(pollId, voteCounts);
        return voteCounts;
    }

    private void cacheVote(Integer pollId, Map<String, Integer> votes) {
        String key = "poll:" + pollId + ":votes";
        Map<String, String> data = new HashMap<>();
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            data.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        if(!data.isEmpty()){
            jedis.hset(key, data);
            Poll poll = pollMap.get(pollId);
            if(poll != null && poll.getValidUntil() != null){
                long ttl = Duration.between(Instant.now(), poll.getValidUntil()).getSeconds();
                if(ttl > 0){
                    jedis.expire(key,ttl);
                }
                else{
                    //some arbitrary time for expiration if no expiration is set
                    jedis.expire(key, 1000);
                }
            }
        }
    }

    public Integer pollIdByVoteOption(Integer voteOptionId) {
        for (Map.Entry<Integer, ArrayList<Integer>> entry : pollOptionIds.entrySet()) {
            if (entry.getValue().contains(voteOptionId)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void deletePollCache(Integer pollId){
        String key = "poll:" + pollId + ":votes";
        jedis.del(key);
    }
}