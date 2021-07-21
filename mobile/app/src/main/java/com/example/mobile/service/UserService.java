package com.example.mobile.service;

import com.example.mobile.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
   /* @POST("register")
    Call<String> register(@Body User user);*/

    @POST("register")
    Call<Void> register(@Body User user);
}
