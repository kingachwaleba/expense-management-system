package com.example.mobile.config;

import com.example.mobile.model.LoginForm;
import com.example.mobile.model.User;
import com.example.mobile.model.Wallet;
import com.example.mobile.model.WalletModel;
import com.google.gson.JsonObject;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface  {
    @POST("login")
    Call<JsonObject> login(@Body LoginForm loginForm);

    @POST("register")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> register(@Body User user);

    @GET("wallets")
    @Headers("Content-Type: application/json")
    Call<List<Wallet>> getUserWallets(@Header("Authorization") String accessToken);

    @GET("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<WalletModel> getWalletById(@Header("Authorization") String accessToken, @Path("id") int id);
}