package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("quantity")
    double quantity;
    @SerializedName("unit")
    Unit unit;
    @SerializedName("status")
    Status status;
    @SerializedName("user")
    User user;

    public Product(int id, String name, double quantity, Unit unit, Status status, User user) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
        this.user = user;
    }

    public Product(String name, double quantity, Unit unit, Status status, User user) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.status = status;
        this.user = user;
    }

    public Product(String name, double quantity, Unit unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Product(String name, double quantity, Unit unit, User user) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.user = user;
    }

    public Product(int id, String name, double quantity, Unit unit, User user) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
