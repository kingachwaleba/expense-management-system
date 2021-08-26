package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCreate {
    @SerializedName("name")
    String name;
    @SerializedName("listDetailList")
    List<Product> listDetailList;

    public ListCreate(String name, List<Product> listDetailList) {
        this.name = name;
        this.listDetailList = listDetailList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getListDetailList() {
        return listDetailList;
    }

    public void setListDetailList(List<Product> listDetailList) {
        this.listDetailList = listDetailList;
    }
}
