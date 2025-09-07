package com.example.dat250_1.model;

import java.util.ArrayList;

public class VoteOption {
    private Integer id;
    private String caption;
    private Integer presentationOrder;
    private final ArrayList<Integer> voteIds = new ArrayList<>();

    public VoteOption() {}

    public VoteOption(Integer id, String caption, Integer presentationOrder){
        this.id = id;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    public String getCaption(){
        return caption;
    }

    public int getPresentationOrder(){
        return presentationOrder;
    }

    public ArrayList<Integer> getVoteIds() {
        return voteIds;
    }

}
