package com.example.mobile.config;

import com.example.mobile.model.Category;
import com.example.mobile.model.DebtsHolder;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import com.example.mobile.model.Invitation;
import com.example.mobile.model.ListCreate;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.Member;
import com.example.mobile.model.Message;
import com.example.mobile.model.Product;
import com.example.mobile.model.RegistrationForm;
import com.example.mobile.model.Status;
import com.example.mobile.model.Unit;
import com.example.mobile.model.UpdatePasswordHolder;
import com.example.mobile.model.User;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //User
    @POST("login")
    Call<JsonObject> login(@Body LoginForm loginForm);

    @POST("register")
    Call<ResponseBody> register(@Body RegistrationForm registrationForm);

    @POST("account/forgot-password")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> restorePassword(@Query("email") String email);

    //Account
    @GET("account")
    @Headers("Content-Type: application/json")
    Call<User> getAccount(@Header("Authorization") String accessToken);

    @PUT("account/change-password")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> changePassword(@Header("Authorization") String accessToken, @Body UpdatePasswordHolder updatePasswordHolder);

    @Multipart
    @PUT("account/change-profile-picture")
    Call<String> uploadProfileImage(@Header("Authorization") String accessToken, @Part MultipartBody.Part image);

    @PUT("account/delete-profile-picture")
    Call<ResponseBody> deleteProfileImage(@Header("Authorization") String accessToken);

    @PUT("delete-account")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteAccount(@Header("Authorization") String accessToken, @Body String password);

    //Notification
    @GET("notifications/invitations")
    @Headers("Content-Type: application/json")
    Call<List<Invitation>> getInvitations(@Header("Authorization") String accessToken);

    @GET("debts-notifications")
    @Headers("Content-Type: application/json")
    Call<List<Message>> getDebtNotification(@Header("Authorization") String accessToken);

    @PUT("notifications/invitations/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> manageInvitation(@Header("Authorization") String accessToken, @Path("id") int id, @Body Boolean flag);

    @DELETE("notifications/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteNotification(@Header("Authorization") String accessToken, @Path("id") int id);

    //ValidationTable
    @GET("wallet-categories")
    @Headers("Content-Type: application/json")
    Call<List<Category>> getWalletCategories();

    @GET("categories")
    @Headers("Content-Type: application/json")
    Call<List<Category>> getExpenseCategories();

    @GET("units")
    @Headers("Content-Type: application/json")
    Call<List<Unit>> getUnits();

    @GET("statues")
    @Headers("Content-Type: application/json")
    Call<List<Status>> getStatuses();

    //Wallet
    @GET("wallets")
    @Headers("Content-Type: application/json")
    Call<List<WalletCreate>> getUserWallets(@Header("Authorization") String accessToken);

    @GET("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<WalletCreate> getWalletById(@Header("Authorization") String accessToken, @Path("id") int id);

    @POST("create-wallet")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createWallet(@Header("Authorization") String accessToken, @Body WalletHolder walletHolder);

    @PUT("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editWallet(@Header("Authorization") String accessToken, @Path("id") int id, @Body WalletCreate wallet);

    @DELETE("wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteWallet(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("wallet/{id}/stats/{dateFrom}/{dateTo}")
    @Headers("Content-Type: application/json")
    Call<Map<String, Object>> getStats(@Header("Authorization") String accessToken, @Path("id") int id, @Path("dateFrom") String dateFrom, @Path("dateTo") String dateTo);

    //Members
    @GET("find-users/{infix}")
    @Headers("Content-Type: application/json")
    Call<List<Member>> getMembersByInfix(@Header("Authorization") String accessToken, @Path("infix") String infix);

    @GET("wallet/{id}/{infix}")
    @Headers("Content-Type: application/json")
    Call<List<Member>> getMembersByInfixInWallet(@Header("Authorization") String accessToken, @Path("id") int id, @Path("infix") String infix);

    @PUT("wallet/{id}/users/{userLogin}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendInvitationToUser(@Header("Authorization") String accessToken, @Path("id") int id, @Path("userLogin") String userLogin);

    @DELETE("wallet/{id}/user/{userLogin}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteMember(@Header("Authorization") String accessToken, @Path("id") int id, @Path("userLogin") String userLogin);

    @DELETE("wallet/{id}/current-logged-in-user")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteCurrentMember(@Header("Authorization") String accessToken, @Path("id") int id);

    //ShoppingList
    @GET("wallet/{id}/shopping-lists")
    @Headers("Content-Type: application/json")
    Call<List<ListShop>> getWalletLists(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("shopping-list/{id}")
    @Headers("Content-Type: application/json")
    Call<ListShop> getListById(@Header("Authorization") String accessToken, @Path("id") int id);

    @POST("wallet/{id}/create-shopping-list")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createList(@Header("Authorization") String accessToken, @Path("id") int id, @Body ListCreate list);

    @PUT("shopping-list/edit/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editListName(@Header("Authorization") String accessToken, @Path("id") int id, @Body String name);

    @PUT("change-list-status/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> changeListStatus(@Header("Authorization") String accessToken, @Path("id") int id, @Body int statusId);

    @DELETE("/shopping-list/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteList(@Header("Authorization") String accessToken, @Path("id") int id);

    @POST("shopping-list/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> addListItem(@Header("Authorization") String accessToken, @Path("id") int id, @Body Product product);

    @PUT("edit-list-element/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editListItem(@Header("Authorization") String accessToken, @Path("id") int id, @Body Product product);

    @PUT("change-element-status/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> changeListElementStatus(@Header("Authorization") String accessToken, @Path("id") int id, @Body int statusId);

    @DELETE("delete-list-element/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteListElement(@Header("Authorization") String accessToken, @Path("id") int id);

    //Expense
    @GET("wallet/{id}/expenses")
    @Headers("Content-Type: application/json")
    Call<List<Expense>> getAllExpense(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("expense/{id}")
    @Headers("Content-Type: application/json")
    Call<ExpenseHolder> getExpenseById(@Header("Authorization") String accessToken, @Path("id") int id);

    @POST("wallet/{id}/add-expense")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createExpense(@Header("Authorization") String accessToken, @Path("id") int id, @Body ExpenseHolder expenseHolder);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadReceiptImage(@Header("Authorization") String accessToken, @Part MultipartBody.Part image);

    @PUT("expense/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editExpenseById(@Header("Authorization") String accessToken, @Path("id") int id, @Body ExpenseHolder expense);

    @DELETE("expense/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteExpense(@Header("Authorization") String accessToken, @Path("id") int id);

    @PUT("pay-debt/wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> payDebt(@Header("Authorization") String accessToken, @Path("id") int id, @Body DebtsHolder debtsHolder);

    @POST("send-notification/wallet/{id}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendDebtNotification(@Header("Authorization") String accessToken, @Path("id") int id, @Body DebtsHolder debtsHolder);

    //Chat
    @GET("wallet/{id}/message/{stringDate}")
    @Headers("Content-Type: application/json")
    Call<List<Message>> getMessages(@Header("Authorization") String accessToken, @Path("id") int id, @Path("stringDate") String date);

    @POST("wallet/{id}/message")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendMessage(@Header("Authorization") String accessToken, @Path("id") int id, @Body Message message);

    @GET("message-notifications")
    @Headers("Content-Type: application/json")
    Call<List<Message>> getMessageNotification(@Header("Authorization") String accessToken);

    @DELETE("notifications/{id}/messages")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> deleteMessageNotification(@Header("Authorization") String accessToken, @Path("id") int id);
}