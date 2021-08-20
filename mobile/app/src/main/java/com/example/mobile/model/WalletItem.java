package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class WalletItem {
    @SerializedName("walletId")
    int walletId;
    @SerializedName("name")
    String name;
    @SerializedName("owner")
    String owner;
    @SerializedName("userListCounter")
    int userListCounter;
    // Double balance;

    public WalletItem(int id, String name, String owner, int numeberOfMembers, Double balance) {
        this.name = name;
        this.owner = owner;
        this.userListCounter = numeberOfMembers;
        //this.balance = balance;
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
}
