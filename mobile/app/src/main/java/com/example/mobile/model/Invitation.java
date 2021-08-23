package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Invitation {
    @SerializedName("walletId")
    private int walletId;
    @SerializedName("walletUserId")
    private int walletUserId;
    @SerializedName("name")
    private String name;
    @SerializedName("owner")
    private String owner;
    @SerializedName("userListCounter")
    private String userListCounter;

    public Invitation(int walletId, int walletUserId, String name, String owner, String userListCounter) {
        this.walletId = walletId;
        this.walletUserId = walletUserId;
        this.name = name;
        this.owner = owner;
        this.userListCounter = userListCounter;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getWalletUserId() {
        return walletUserId;
    }

    public void setWalletUserId(int walletUserId) {
        this.walletUserId = walletUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUserListCounter() {
        return userListCounter;
    }

    public void setUserListCounter(String userListCounter) {
        this.userListCounter = userListCounter;
    }
}
