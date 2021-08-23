package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WalletHolder {
    @SerializedName("wallet")
    private WalletCreate walletCreate;
    @SerializedName("userList")
    private List<String> userList;

    public WalletHolder(WalletCreate walletCreate, List<String> userList) {
        this.walletCreate = walletCreate;
        this.userList = userList;
    }

    public WalletCreate getWalletCreate() {
        return walletCreate;
    }

    public void setWalletCreate(WalletCreate walletCreate) {
        this.walletCreate = walletCreate;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
