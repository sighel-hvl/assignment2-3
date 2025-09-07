package com.example.dat250_1.model;


import java.time.Instant;
import java.util.ArrayList;

public class Poll {
    private Integer id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private ArrayList<String> voteOptionList;
    private Integer userId;


    public Poll() {}

    public Poll(Integer id, String question, Instant publishedAt, Instant validUntil,  ArrayList<String> voteOptionList, Integer userId) {
        this.id = id;
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.voteOptionList = voteOptionList;
        this.userId = userId;
    }

    public String getQuestion(){
        return question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public Instant getValidUntil(){
        return validUntil;
    }

    public ArrayList<String> getVoteOptionList() {return voteOptionList;}

    public Integer getId() {return id;}
}
