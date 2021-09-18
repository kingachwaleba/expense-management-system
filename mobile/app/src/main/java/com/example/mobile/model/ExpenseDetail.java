package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class ExpenseDetail {
    @SerializedName("id")
    int id;
    @SerializedName("expense")
    Expense expense;
    @SerializedName("user")
    Member member;
    @SerializedName("cost")
    Double cost;

    public ExpenseDetail(int id, Member member, Double cost) {
        this.id = id;
        this.member = member;
        this.cost = cost;
    }

    public ExpenseDetail(int id, Expense expense, Member member, Double cost) {
        this.id = id;
        this.expense = expense;
        this.member = member;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }
}
