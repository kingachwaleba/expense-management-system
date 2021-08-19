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

    public WalletService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnWalletCallback{
        void onOneWallet(WalletModel walletModel);
    }

    public interface OnMemberSearchCallback{
        void onMembersList(List<String> members);
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

    public WalletModel getWalletById(OnWalletCallback callback, String accessToken, int id) {
        WalletModel walletModel = new WalletModel(0, " ", " ", " ", 0, null);
        Call<WalletModel> call = apiInterface.getWalletById("Bearer " + accessToken, id);
        call.enqueue(new Callback<WalletModel>() {
            @Override
            public void onResponse(@NotNull Call<WalletModel> call, @NotNull Response<WalletModel> response) {
                callback.onOneWallet(response.body());
            }
            @Override
            public void onFailure(@NotNull Call<WalletModel> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
        return walletModel;
    }

    public void getMembersByInfix(WalletService.OnMemberSearchCallback callback, String infix){
        System.out.println(infix);
        Call<List<String>> call = apiInterface.getMembersByInfix(infix);
        System.out.println(infix);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                callback.onMembersList(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }
}
