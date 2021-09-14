package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class DebtsHolder {
    @SerializedName("debtor")
    private User debtor;
    @SerializedName("creditor")
    private User creditor;
    @SerializedName("howMuch")
    private Double howMuch;

    public DebtsHolder(User debtor, User creditor, Double howMuch) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.howMuch = howMuch;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public Double getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(Double howMuch) {
        this.howMuch = howMuch;
    }
}
