package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Member {
    @SerializedName("userId")
    private int userId;
    @SerializedName("login")
    private String login;

    public Member(int userId, String login) {
        this.userId = userId;
        this.login = login;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
