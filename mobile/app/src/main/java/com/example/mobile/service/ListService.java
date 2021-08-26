package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.ListCreate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListService {

    Context context;
    ApiInterface apiInterface;


    public ListService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public void createList(String accessToken, int id, Map<String, ListCreate> list){
        Call<ResponseBody> call = apiInterface.createList("Bearer " + accessToken, id, list);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.d("CREATE_LIST", response.toString());
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
