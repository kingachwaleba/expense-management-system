package com.example.mobile.service;

import android.content.Context;;
import android.widget.Toast;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.Category;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidationTableService  {
    Context context;
    ApiInterface apiInterface;

    public ValidationTableService (Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnValidationTable{
        void onCategories(List<Category> categories);
    }

    public void getCategories(OnValidationTable callback){
        Call<List<Category>> call = apiInterface.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NotNull Call<List<Category>> call, @NotNull Response<List<Category>> response) {
                callback.onCategories(response.body());
            }
            @Override
            public void onFailure(@NotNull Call<List<Category>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
