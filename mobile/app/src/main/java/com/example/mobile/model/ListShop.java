package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListShop {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("wallet")
    WalletCreate walletCreate;
    @SerializedName("status")
    Status status;
    @SerializedName("user")
    User user;
    @SerializedName("listDetailSet")
    List<Product> listDetailSet;

    public ListShop(int id, String name, WalletCreate walletCreate, Status status, User user, List<Product> listDetailSet) {
        this.id = id;
        this.name = name;
        this.walletCreate = walletCreate;
        this.status = status;
        this.user = user;
        this.listDetailSet = listDetailSet;
    }

    public ListShop(String name, List<Product> listDetailSet) {
        this.name = name;
        this.listDetailSet = listDetailSet;
    }

    public ListShop(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WalletCreate getWalletCreate() {
        return walletCreate;
    }

    public void setWalletCreate(WalletCreate walletCreate) {
        this.walletCreate = walletCreate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getListDetailSet() {
        return listDetailSet;
    }

    public void setListDetailSet(List<Product> listDetailSet) {
        this.listDetailSet = listDetailSet;
    }

}
