package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Status(String name) {
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
}
