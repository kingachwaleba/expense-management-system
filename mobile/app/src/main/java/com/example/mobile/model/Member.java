package com.example.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Member implements Parcelable {
    @SerializedName("balance")
    Double balance;
    @SerializedName("login")
    String login;
    @SerializedName("image")
    String image;
    @SerializedName("userId")
    int userId;
    @SerializedName("debt")
    DebtsHolder debt;

    public Member(String login, String image) {
        this.login = login;
        this.image = image;
    }

    public Member(String login, String image, int userId) {
        this.login = login;
        this.image = image;
        this.userId = userId;
    }

    public Member(Double balance, String login, int userId, DebtsHolder debt) {
        this.balance = balance;
        this.login = login;
        this.userId = userId;
        this.debt = debt;
    }

    protected Member(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        login = in.readString();
        userId = in.readInt();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public DebtsHolder getDebt() {
        return debt;
    }

    public void setDebt(DebtsHolder debt) {
        this.debt = debt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(balance);
        }
        dest.writeString(login);
        dest.writeInt(userId);
    }

}
