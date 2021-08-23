package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.WalletHolder;
import com.example.mobile.model.WalletDetail;
import com.example.mobile.model.WalletItem;
import com.example.mobile.service.adapter.WalletAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletService {

    Context context;
    ApiInterface apiInterface;
    RecyclerView wallet_rv;

    public WalletService(Context context, RecyclerView wallet_rv) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
        this.wallet_rv = wallet_rv;
    }

    public WalletService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnWalletCallback{
        void onOneWallet(WalletDetail walletDetail);
    }

    public interface OnMemberSearchCallback{
        void onMembersList(List<String> members);
    }

    public void getUserWallets(String accessToken) {
        Call<List<WalletItem>> call = apiInterface.getUserWallets("Bearer " + accessToken);
        call.enqueue(new Callback<List<WalletItem>>() {
            @Override
            public void onResponse(@NotNull Call<List<WalletItem>> call, @NotNull Response<List<WalletItem>> response) {
                    try {
                        if (response.isSuccessful()) {
                            List<WalletItem> walletItems = response.body();
                            WalletAdapter walletAdapter = new WalletAdapter(context, walletItems);
                            wallet_rv.setAdapter(walletAdapter);
                            walletAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
            }
            @Override
            public void onFailure(@NotNull Call<List<WalletItem>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getWalletById(OnWalletCallback callback, String accessToken, int id) {
        Call<WalletDetail> call = apiInterface.getWalletById("Bearer " + accessToken, id);
        call.enqueue(new Callback<WalletDetail>() {
            @Override
            public void onResponse(@NotNull Call<WalletDetail> call, @NotNull Response<WalletDetail> response) {
                callback.onOneWallet(response.body());
            }
            @Override
            public void onFailure(@NotNull Call<WalletDetail> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void createWallet(String accessToken, WalletHolder walletHolder) {
        Call<ResponseBody> call = apiInterface.createWallet("Bearer " + accessToken, walletHolder);
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

    public void getMembersByInfix(WalletService.OnMemberSearchCallback callback, String infix){
        Call<List<String>> call = apiInterface.getMembersByInfix(infix);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                callback.onMembersList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
