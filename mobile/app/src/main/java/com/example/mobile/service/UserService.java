package com.example.mobile.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.example.mobile.LoginActivity;
import com.example.mobile.MainActivity;
import com.example.mobile.RegistrationActivity;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.User;
import com.example.mobile.model.WalletModel;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class UserService {
    Context context;
    SessionManager session;
    ApiInterface apiInterface;

    public UserService(Context context) {
        this.context = context;
        this.session = new SessionManager(context);
        this.apiInterface = new ApiClient().getService();
    }

    public void register(User user) {
        Call<ResponseBody> call = apiInterface.register(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                ((RegistrationActivity)context).finish();
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Nie udało się zarejestrować użytkownika",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void login(LoginForm loginForm) {
        Call<JsonObject> call = apiInterface.login(loginForm);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if(response.body()!=null){
                    String login =response.body().get("login").toString();
                    String token =response.body().get("token").toString();
                    login = login.substring(1, login.length() - 1);
                    token = token.substring(1, token.length() - 1);
                    session.createLoginSession(login, token);
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    ((LoginActivity)context).finish();
                }
            }
            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(context,"Logowanie nie powiodło się",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
