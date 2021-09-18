package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private int id;
    @SerializedName("receiver")
    private User receiver;
    @SerializedName("wallet")
    private WalletCreate wallet;
    @SerializedName("date")
    private String date;
    @SerializedName("content")
    private String content;
    @SerializedName("type")
    private String type;
    @SerializedName("sender")
    private User sender;

    public Message(String content) {
        this.content = content;
    }

    public Message(int id, WalletCreate wallet, User sender) {
        this.id = id;
        this.wallet = wallet;
        this.sender = sender;
    }

    public Message(User receiver, WalletCreate wallet, String date, String content, String type, User sender) {
        this.receiver = receiver;
        this.wallet = wallet;
        this.date = date;
        this.content = content;
        this.type = type;
        this.sender = sender;
    }

    public Message(int id, User receiver, WalletCreate wallet, String date, String content, String type, User sender) {
        this.id = id;
        this.receiver = receiver;
        this.wallet = wallet;
        this.date = date;
        this.content = content;
        this.type = type;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public WalletCreate getWallet() {
        return wallet;
    }

    public void setWallet(WalletCreate wallet) {
        this.wallet = wallet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
