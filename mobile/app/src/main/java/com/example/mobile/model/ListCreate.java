package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCreate {
    @SerializedName("list")
    ListShop listShop;
    @SerializedName("listDetailList")
    List<Product> listDetailList;

    public ListCreate(ListShop listShop, List<Product> listDetailList) {
        this.listShop = listShop;
        this.listDetailList = listDetailList;
    }

    public ListShop getListShop() {
        return listShop;
    }

    public void setListShop(ListShop listShop) {
        this.listShop = listShop;
    }

    public List<Product> getListDetailList() {
        return listDetailList;
    }

    public void setListDetailList(List<Product> listDetailList) {
        this.listDetailList = listDetailList;
    }
}
