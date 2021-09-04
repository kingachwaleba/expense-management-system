package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.HashSet;

public class Expense {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("date")
    String date;
    @SerializedName("receipt_image")
    String receipt_image;
    @SerializedName("total_cost")
    Double total_cost;
    @SerializedName("period")
    String period;
    @SerializedName("category")
    Category category;
    @SerializedName("wallet")
    WalletCreate wallet;
    @SerializedName("user")
    User user;
    @SerializedName("expenseDetailSet")
    HashSet<ExpenseDetail> expenseDetailsSet;

    public Expense(int id, String name, String date, String receipt_image, Double total_cost, String period, Category category, WalletCreate wallet, User user, HashSet<ExpenseDetail> expenseDetailsSet) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.receipt_image = receipt_image;
        this.total_cost = total_cost;
        this.period = period;
        this.category = category;
        this.wallet = wallet;
        this.user = user;
        this.expenseDetailsSet = expenseDetailsSet;
    }

    public Expense(String name, String receipt_image, Double total_cost, String period, Category category) {
        this.name = name;
        this.receipt_image = receipt_image;
        this.total_cost = total_cost;
        this.period = period;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceipt_image() {
        return receipt_image;
    }

    public void setReceipt_image(String receipt_image) {
        this.receipt_image = receipt_image;
    }

    public Double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(Double total_cost) {
        this.total_cost = total_cost;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WalletCreate getWallet() {
        return wallet;
    }

    public void setWallet(WalletCreate wallet) {
        this.wallet = wallet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashSet<ExpenseDetail> getExpenseDetailsSet() {
        return expenseDetailsSet;
    }

    public void setExpenseDetailsSet(HashSet<ExpenseDetail> expenseDetailsSet) {
        this.expenseDetailsSet = expenseDetailsSet;
    }
}
