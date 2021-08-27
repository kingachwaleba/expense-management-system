package com.example.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    @SerializedName("id")
    int id;
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

    public User(int id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
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
        /*email = in.readString();
        password = in.readString();
        image = in.readString();
        walletsNumber = in.readInt();*/
    }

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
        /*dest.writeString(email);
        dest.writeString(password);
        dest.writeString(image);
        dest.writeInt(walletsNumber);*/
    }
}
