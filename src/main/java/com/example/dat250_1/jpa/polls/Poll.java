package com.example.dat250_1.jpa.polls;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteOption> options = new ArrayList<>();

    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
    }

    public Poll() {
    }

    public VoteOption addVoteOption(String option) {
        VoteOption opt = new VoteOption(option, this);
        opt.setPresentationOrder(options.size());
        options.add(opt);
        return opt;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public User getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    public List<VoteOption> getOptions() {
        return options;
    }
}
