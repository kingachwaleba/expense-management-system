package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class WalletCreate {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("walletCategory")
    private Category category;

    public WalletCreate(int id, String name,String description, Category category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public WalletCreate(String name, String description, Category category) {
        this.name = name;
        this.category = category;
        this.description = description;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

