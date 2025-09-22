package com.example.dat250_1.jpa.polls;

import jakarta.persistence.*;

@Entity
public class Vote {
    public Vote() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private VoteOption votesOn;


    public Vote(VoteOption option, User user) {
        this.votesOn = option;
        this.user = user;
    }
}
