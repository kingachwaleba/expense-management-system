package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpenseHolder {
    @SerializedName("expense")
    Expense expense;
    @SerializedName("userList")
    List<String> userList;
    @SerializedName("deletedUserList")
    List<String> deletedUserList;

    public ExpenseHolder(Expense expense, List<String> userList) {
        this.expense = expense;
        this.userList = userList;
    }

    public ExpenseHolder(List<String> deletedUserList, Expense expense) {
        this.expense = expense;
        this.deletedUserList = deletedUserList;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getDeletedUserList() {
        return deletedUserList;
    }

    public void setDeletedUserList(List<String> deletedUserList) {
        this.deletedUserList = deletedUserList;
    }
}
