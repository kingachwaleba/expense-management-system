package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class RegistrationForm {
    @SerializedName("user")
    User user;
    @SerializedName("confirmPassword")
    String confirmPassword;

    public RegistrationForm(User user, String confirmPassword) {
        this.user = user;
        this.confirmPassword = confirmPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
