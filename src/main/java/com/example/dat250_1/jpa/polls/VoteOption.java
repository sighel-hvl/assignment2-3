package com.example.dat250_1.jpa.polls;

import jakarta.persistence.*;

@Entity
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;
    private int presentationOrder;
    public VoteOption() {
    }

    public VoteOption(String option, Poll poll) {
        this.caption = option;
        this.poll = poll;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
}
