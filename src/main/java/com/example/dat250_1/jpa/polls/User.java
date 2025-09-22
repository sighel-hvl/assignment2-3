package com.example.dat250_1.jpa.polls;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poll> polls = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();


    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    public User() {
    }

    public Poll createPoll(String question){
        Poll poll = new Poll(question, this);
        polls.add(poll);
        return poll;
    }

    public Vote voteFor(VoteOption option){
        Vote vote = new Vote(option, this);
        votes.add(vote);
        return vote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
}
