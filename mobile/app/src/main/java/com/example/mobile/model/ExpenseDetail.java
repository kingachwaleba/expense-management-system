package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class ExpenseDetail {
    @SerializedName("id")
    int id;
    @SerializedName("paymentStatus")
    Status status;
    @SerializedName("user")
    Member member;
    @SerializedName("cost")
    Double cost;

    public ExpenseDetail(int id, Status status, Member member, Double cost) {
        this.id = id;
        this.status = status;
        this.member = member;
        this.cost = cost;
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
}
