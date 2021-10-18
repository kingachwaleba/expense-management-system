package com.example.mobile.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mobile.activity.LoginActivity;
import com.example.mobile.activity.MainActivity;
import com.example.mobile.activity.RegistrationActivity;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.ErrorUtils;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.RegistrationForm;
import com.example.mobile.model.User;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    Context context;
    SessionManager session;
    ApiInterface apiInterface;

    public UserService(Context context) {
        this.context = context;
        this.session = new SessionManager(context);
        this.apiInterface = new ApiClient().getService();
    }

    public void login(LoginForm loginForm) {
        Call<JsonObject> call = apiInterface.login(loginForm);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response){
                if (response.isSuccessful()) {
                    if (response.body() != null && response.code() == 200) {
                        String login = response.body().get("login").getAsString();
                        String token = response.body().get("token").getAsString();
                        String expiryDate = response.body().get("expiryDate").getAsString();
                        String image = response.body().get("image").getAsString();
                        session.createLoginSession(login, token, expiryDate, image);
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                        ((LoginActivity) context).finish();
                    } else Toast.makeText(context, "Nie poprawne dane logowania", Toast.LENGTH_LONG).show();
                } else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(context, "Logowanie nie powiodło się", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void register(RegistrationForm registrationForm) {
        Call<ResponseBody> call = apiInterface.register(registrationForm);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response){
                if (response.isSuccessful()) {
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    ((RegistrationActivity) context).finish();
                } else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Nie udało się zarejestrować użytkownika", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }


    public void restorePassword(String email) {
        Call<ResponseBody> call = apiInterface.restorePassword(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful())
                    Toast.makeText(context, "Sprawdź skrzynkę pocztową!", Toast.LENGTH_LONG).show();
                else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Nie udało się", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
