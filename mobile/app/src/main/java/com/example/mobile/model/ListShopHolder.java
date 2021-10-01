package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListShopHolder {
    @SerializedName("shoppingList")
    ListShop listShop;
    @SerializedName("deletedUserList")
    List<String> deletedUserList;

    public ListShopHolder(ListShop listShop, List<String> deletedUserList) {
        this.listShop = listShop;
        this.deletedUserList = deletedUserList;
    }

    public ListShop getListShop() {
        return listShop;
    }

    public void setListShop(ListShop listShop) {
        this.listShop = listShop;
    }

    public List<String> getDeletedUserList() {
        return deletedUserList;
    }

    public void setDeletedUserList(List<String> deletedUserList) {
        this.deletedUserList = deletedUserList;
    }
}
