package com.example.dat250_1.model;

import java.util.ArrayList;

public class User {
    private Integer id;
    private String username;
    private String email;
    private ArrayList<Integer> polls = new ArrayList<>();
    private ArrayList<Integer> votes = new ArrayList<>();

    public User() {}

    public User(Integer id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId(){
        return id;
    }

    public ArrayList<Integer> getVoteIds() {return votes;}

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public ArrayList<Integer> getPollIds() { return polls; }
}
