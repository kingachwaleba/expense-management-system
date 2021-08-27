package com.example.mobile.config;

import com.example.mobile.model.Category;
import com.example.mobile.model.Invitation;
import com.example.mobile.model.ListCreate;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.Product;
import com.example.mobile.model.Unit;
import com.example.mobile.model.UpdatePasswordHolder;
import com.example.mobile.model.User;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface  {
    @POST("login")
    Call<JsonObject> login(@Body LoginForm loginForm);

    @POST("register")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> register(@Body User user);

    @PUT("account/change-password")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> changePassword(@Header("Authorization") String accessToken, @Body UpdatePasswordHolder updatePasswordHolder);

    @GET("account")
    @Headers("Content-Type: application/json")
    Call<User> getAccount(@Header("Authorization") String accessToken);

    @GET("notifications/invitations")
    @Headers("Content-Type: application/json")
    Call<List<Invitation>> getInvitations(@Header("Authorization") String accessToken);

    @PUT("notifications/invitations/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> manageInvitation(@Header("Authorization") String accessToken, @Path("id") int id, @Body Boolean flag);

    @GET("wallets")
    @Headers("Content-Type: application/json")
    Call<List<WalletCreate>> getUserWallets(@Header("Authorization") String accessToken);

    @POST("create-wallet")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createWallet(@Header("Authorization") String accessToken, @Body WalletHolder walletHolder);

    @POST("wallet/{id}/create-shopping-list")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createList(@Header("Authorization") String accessToken, @Path("id") int id, @Body ListCreate list);

    @GET("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<WalletCreate> getWalletById(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("wallet-categories")
    @Headers("Content-Type: application/json")
    Call<List<Category>> getWalletCategories();

    @GET("units")
    @Headers("Content-Type: application/json")
    Call<List<Unit>> getUnits();

    @GET("{infix}")
    @Headers("Content-Type: application/json")
    Call<List<String>> getMembersByInfix(@Header("Authorization") String accessToken, @Path("infix") String infix);

    @GET("wallet/{id}/{infix}")
    @Headers("Content-Type: application/json")
    Call<List<String>> getMembersByInfixInWallet(@Header("Authorization") String accessToken, @Path("id") int id, @Path("infix") String infix);

    @PUT("wallet/{id}/users/{userLogin}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendInvitationToUser(@Header("Authorization") String accessToken, @Path("id") int id, @Path("userLogin") String userLogin);


    @PUT("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editWallet(@Header("Authorization") String accessToken, @Path("id") int id, @Body Map<String, String> map);


    @GET("wallet/{id}/shopping-lists")
    @Headers("Content-Type: application/json")
    Call<List<ListShop>> getWalletLists(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("shopping-list/{id}")
    @Headers("Content-Type: application/json")
    Call<ListShop> getListById(@Header("Authorization") String accessToken, @Path("id") int id);

    @POST("shopping-list/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> addListItem(@Header("Authorization") String accessToken, @Path("id") int id, @Body Product product);

    @PUT("edit-list-element/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editListItem(@Header("Authorization") String accessToken, @Path("id") int id, @Body Product product);

    @PUT("shopping-list/edit/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editListName(@Header("Authorization") String accessToken, @Path("id") int id, @Body String name);


}