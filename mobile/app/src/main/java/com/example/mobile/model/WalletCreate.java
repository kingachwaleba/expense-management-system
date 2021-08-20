package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class WalletCreate {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("walletCategory")
    private Category category;

    public WalletCreate(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

