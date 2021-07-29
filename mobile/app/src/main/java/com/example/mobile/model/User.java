package com.example.mobile.model;


public class User {
    private String login;
    private String email;
    private String image;
    private String password;
    private String status;

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.image = null;
        this.password = password;
    }

    public User(String login, String status) {
        this.login = login;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
