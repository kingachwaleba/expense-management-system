package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("name")
    String name;
    @SerializedName("quantity")
    double quantity;
    @SerializedName("unit")
    Unit unit;

    public Product(String name, double quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
