package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("id")
    private int id;
    @SerializedName("login")
    private String login;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("userListCounter")
    private int userListCounter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUserListCounter() {
        return userListCounter;
    }

    public void setUserListCounter(int userListCounter) {
        this.userListCounter = userListCounter;
    }
}
