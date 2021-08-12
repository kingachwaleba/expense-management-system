package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.Wallet;
import com.example.mobile.model.WalletModel;

import org.jetbrains.annotations.NotNull;
import java.util.List;
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

    public void getUserWallets(String accessToken) {
        Call<List<Wallet>> call = apiInterface.getUserWallets("Bearer " + accessToken);
        call.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(@NotNull Call<List<Wallet>> call, @NotNull Response<List<Wallet>> response) {
                    try {
                        if (response.isSuccessful()) {
                            List<Wallet> wallets = response.body();
                            WalletAdapter walletAdapter = new WalletAdapter(context, wallets);
                            wallet_rv.setAdapter(walletAdapter);
                            walletAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
            }
            @Override
            public void onFailure(@NotNull Call<List<Wallet>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getWalletById(String accessToken, int id) {
        Call<WalletModel> call = apiInterface.getWalletById("Bearer " + accessToken, id);
        call.enqueue(new Callback<WalletModel>() {
            @Override
            public void onResponse(@NotNull Call<WalletModel> call, @NotNull Response<WalletModel> response) {
                try {
                    if (response.isSuccessful()) {
                        WalletModel walletModel = response.body();

                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
            @Override
            public void onFailure(@NotNull Call<WalletModel> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
