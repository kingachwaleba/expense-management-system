package com.example.mobile.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static ApiInterface apiInterface;
    private static Retrofit retrofit;

    public ApiClient(){
        String BASE_URL = "http://192.168.0.31:8080/";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public ApiInterface getService(){
        return apiInterface;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}