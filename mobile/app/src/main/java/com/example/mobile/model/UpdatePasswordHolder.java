package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordHolder {
    @SerializedName("password")
    private String password;
    @SerializedName("oldPassword")
    private String oldPassword;
    @SerializedName("confirmPassword")
    private final String confirmPassword;

    public UpdatePasswordHolder(String password, String oldPassword, String confirmPassword) {
        this.password = password;
        this.oldPassword = oldPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
