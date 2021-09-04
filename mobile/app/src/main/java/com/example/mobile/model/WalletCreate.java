package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

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
    List<User> userList;


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

    public WalletCreate(int id, String name, String description, Category category, String owner, int userListCounter, List<User> userList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.owner = owner;
        this.userListCounter = userListCounter;
        this.userList = userList;
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}

