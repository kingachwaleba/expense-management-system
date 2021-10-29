package com.example.mobile.service;

import android.content.Context;

import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.Category;
import com.example.mobile.model.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidationTableService {

    Context context;
    ApiInterface apiInterface;

    public ValidationTableService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public void getExpenseCategories(OnValidationTableCategory callback) {
        Call<List<Category>> call = apiInterface.getExpenseCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NotNull Call<List<Category>> call, @NotNull Response<List<Category>> response) {
                callback.onCategories(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Category>> call, @NotNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void getWalletCategories(OnValidationTableCategory callback) {
        Call<List<Category>> call = apiInterface.getWalletCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NotNull Call<List<Category>> call, @NotNull Response<List<Category>> response) {
                callback.onCategories(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Category>> call, @NotNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void getUnits(OnValidationTableUnit callback) {
        Call<List<Unit>> call = apiInterface.getUnits();
        call.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(@NotNull Call<List<Unit>> call, @NotNull Response<List<Unit>> response) {
                callback.onUnits(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Unit>> call, @NotNull Throwable t) {
                call.cancel();
            }
        });
    }

    public interface OnValidationTableCategory {
        void onCategories(List<Category> categories);
    }

    public interface OnValidationTableUnit {
        void onUnits(List<Unit> units);
    }

}
