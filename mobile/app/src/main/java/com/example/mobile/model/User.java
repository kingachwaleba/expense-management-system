package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    private String login;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("password")
    private String password;

    public User(String login, String email, String password) {


        this.login = login;
        this.email = email;
        this.image = null;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }
}
