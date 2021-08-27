package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.ListCreate;
import com.example.mobile.model.ListShop;

import org.jetbrains.annotations.NotNull;

import java.util.List;
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

    public interface OnListCallback{
        void onAllList(List<ListShop> lists);
    }

    public interface OnOneListCallback{
        void onOneList(ListShop listShop);
    }

    public void createList(String accessToken, int id, ListCreate list){
        Call<ResponseBody> call = apiInterface.createList("Bearer " + accessToken, id, list);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getAllLists(ListService.OnListCallback callback, String accessToken, int id){
        Call<List<ListShop>> call = apiInterface.getWalletLists("Bearer " + accessToken, id);
        call.enqueue(new Callback<List<ListShop>>() {
            @Override
            public void onResponse(@NotNull Call<List<ListShop>> call, @NotNull Response<List<ListShop>> response) {
                callback.onAllList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<ListShop>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getListById(ListService.OnOneListCallback callback, String accessToken, int id){
        Call<ListShop> call = apiInterface.getListById("Bearer " + accessToken, id);
        call.enqueue(new Callback<ListShop>() {
            @Override
            public void onResponse(@NotNull Call<ListShop> call, @NotNull Response<ListShop> response) {
                callback.onOneList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<ListShop> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

}
