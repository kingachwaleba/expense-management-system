package com.example.mobile.model;

import java.util.List;

public class Wallet {

    String name;
    String owner;
    String category;
    List<User> members;
    int numeberOfMembers;
    Double balance;

    public Wallet(String name, String owner, String category, List<User> members, int numeberOfMembers) {
        this.name = name;
        this.owner = owner;
        this.category = category;
        this.members = members;
        this.numeberOfMembers = numeberOfMembers;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getCategory() {
        return category;
    }

    public List<User> getMembers() {
        return members;
    }

    public int getNumeberOfMembers() {
        return numeberOfMembers;
    }

    public Double getBalance() {
        return balance;
    }
}
