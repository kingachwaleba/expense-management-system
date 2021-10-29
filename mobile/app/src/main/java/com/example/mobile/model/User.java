package com.example.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("id")
    int id;
    @SerializedName("deleted")
    String deleted;
    @SerializedName("roles")
    String roles;
    @SerializedName("userBalance")
    Double userBalance;
    @SerializedName("login")
    private String login;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("image")
    private String image;
    @SerializedName("walletsNumber")
    private int walletsNumber;

    public User(int id, String login, String email, String image, int walletsNumber, Double userBalance) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.image = image;
        this.walletsNumber = walletsNumber;
        this.userBalance = userBalance;
    }

    public User(int id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(int id, String login, String image, String deleted, String roles) {
        this.id = id;
        this.login = login;
        this.image = image;
        this.deleted = deleted;
        this.roles = roles;
    }

    public User(String login) {
        this.login = login;
    }

    public User(int id, String login, String email, int walletsNumber) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.walletsNumber = walletsNumber;
    }

    public User(int id, String login, String email, String image, int walletsNumber) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.image = image;
        this.walletsNumber = walletsNumber;
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public User(int id, String login, String email, String password, String image, int walletsNumber) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.image = image;
        this.walletsNumber = walletsNumber;
    }

    protected User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        image = in.readString();
        deleted = in.readString();
        roles = in.readString();
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public int getWalletsNumber() {
        return walletsNumber;
    }

    public void setWalletsNumber(int walletsNumber) {
        this.walletsNumber = walletsNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(image);
        dest.writeString(deleted);
        dest.writeString(roles);
        dest.writeString(String.valueOf(userBalance));
    }
}
