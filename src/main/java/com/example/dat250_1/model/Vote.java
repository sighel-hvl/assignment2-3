package com.example.dat250_1.model;

import java.time.Instant;

public class Vote {
    private Integer id;
    private Instant publishedAt;
    private Integer voteOptionId;
    private Integer userId;

    public Vote() {}

    public Vote(Integer id, Instant publishedAt, Integer voteOptionId, Integer userId) {
        this.id = id;
        this.publishedAt = publishedAt;
        this.voteOptionId = voteOptionId;
        this.userId = userId;
    }

    public Instant getPublishedAt(){
        return publishedAt;
    }

    public Integer getId() {return id;}

    public Integer getVoteOptionId() {
        return voteOptionId;
    }

    public void setvoteOptionId(Integer voteOptionId) {
        this.voteOptionId = voteOptionId;
    }
}
