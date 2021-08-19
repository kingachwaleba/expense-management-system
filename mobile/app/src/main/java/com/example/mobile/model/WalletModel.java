package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletModel {
    @SerializedName("walletId")
    private int walletId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("owner")
    private String owner;
    @SerializedName("userListCounter")
    private int userListCounter;
    @SerializedName("userList")
    private List<Member> userList;

    public WalletModel(int walletId, String name, String description, String owner, int userListCounter, List<Member> userList) {
        this.walletId = walletId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.userListCounter = userListCounter;
        this.userList = userList;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getUserListCounter() {
        return userListCounter;
    }

    public void setUserListCounter(int userListCounter) {
        this.userListCounter = userListCounter;
    }

    public List<Member> getUserList() {
        return userList;
    }

    public void setUserList(List<Member> userList) {
        this.userList = userList;
    }
}
