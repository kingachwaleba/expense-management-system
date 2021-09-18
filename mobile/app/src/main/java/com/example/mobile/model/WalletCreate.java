package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WalletCreate {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("walletCategory")
    private Category category;
    @SerializedName("owner")
    String owner;
    @SerializedName("userListCounter")
    int userListCounter;
    @SerializedName("userList")
    List<Member> userList;
    @SerializedName("walletExpensesCost")
    Double walletExpensesCost;
    @SerializedName("userExpensesCost")
    Double userExpensesCost;
    @SerializedName("loggedInUserBalance")
    Double loggedInUserBalance;

    public WalletCreate(int id, String name, Category category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public WalletCreate(String name, String description, Category category) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public WalletCreate(int id, String name, Category category, String login, int userListCounter) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.owner = login;
        this.userListCounter = userListCounter;
    }

    public WalletCreate(int id, String name, String description, Category category, String login, int userListCounter) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.owner = login;
        this.userListCounter = userListCounter;
    }

    public WalletCreate(int id, String name, String description, Category category, String owner, int userListCounter, List<Member> userList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.owner = owner;
        this.userListCounter = userListCounter;
        this.userList = userList;
    }

    public WalletCreate(int id, String name, String description, Category category, String owner, int userListCounter, List<Member> userList, Double walletExpensesCost, Double userExpensesCost, Double loggedInUserBalance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.owner = owner;
        this.userListCounter = userListCounter;
        this.userList = userList;
        this.walletExpensesCost = walletExpensesCost;
        this.userExpensesCost = userExpensesCost;
        this.loggedInUserBalance = loggedInUserBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Double getWalletExpensesCost() {
        return walletExpensesCost;
    }

    public void setWalletExpensesCost(Double walletExpensesCost) {
        this.walletExpensesCost = walletExpensesCost;
    }

    public Double getUserExpensesCost() {
        return userExpensesCost;
    }

    public void setUserExpensesCost(Double userExpensesCost) {
        this.userExpensesCost = userExpensesCost;
    }

    public Double getLoggedInUserBalance() {
        return loggedInUserBalance;
    }

    public void setLoggedInUserBalance(Double loggedInUserBalance) {
        this.loggedInUserBalance = loggedInUserBalance;
    }
}

