package com.example.mobile.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiInterface apiInterface;

    public ApiClient(){
        String BASE_URL = "http://192.168.100.8:8080/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getService(){
        return apiInterface;
    }
}