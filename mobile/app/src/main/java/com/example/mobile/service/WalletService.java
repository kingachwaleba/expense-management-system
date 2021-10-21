package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.activity.WalletActivity;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.ErrorUtils;
import com.example.mobile.model.Member;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.example.mobile.service.adapter.WalletAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class WalletService {

    Context context;
    ApiInterface apiInterface;
    RecyclerView walletsRv;

    public WalletService(Context context, RecyclerView walletsRv) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
        this.walletsRv = walletsRv;
    }

    public WalletService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnWalletCallback {
        void onOneWallet(WalletCreate walletCreate);
    }

    public interface OnMemberSearchCallback {
        void onMembersList(List<Member> members);
    }

    public interface OnStatsCallback {
        void onStats(Map<String, Object> response);
    }

    public void getUserWallets(String accessToken) {
        Call<List<WalletCreate>> call = apiInterface.getUserWallets("Bearer " + accessToken);
        call.enqueue(new Callback<List<WalletCreate>>() {
            @Override
            public void onResponse(@NotNull Call<List<WalletCreate>> call, @NotNull Response<List<WalletCreate>> response) {
                try {
                    if (response.isSuccessful()) {
                        List<WalletCreate> walletItems = response.body();
                        WalletAdapter walletAdapter = new WalletAdapter(context, walletItems);
                        walletsRv.setAdapter(walletAdapter);
                        walletAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<WalletCreate>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getWalletById(OnWalletCallback callback, String accessToken, int id) {
        Call<WalletCreate> call = apiInterface.getWalletById("Bearer " + accessToken, id);
        call.enqueue(new Callback<WalletCreate>() {
            @Override
            public void onResponse(@NotNull Call<WalletCreate> call, @NotNull Response<WalletCreate> response) {
                callback.onOneWallet(response.body());
            }
            @Override
            public void onFailure(@NotNull Call<WalletCreate> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void createWallet(String accessToken, WalletHolder walletHolder) {
        Call<ResponseBody> call = apiInterface.createWallet("Bearer " + accessToken, walletHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void updateWallet(String accessToken, int id, WalletCreate wallet) {
        Call<ResponseBody> call = apiInterface.editWallet("Bearer " + accessToken, id, wallet);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteWallet(String accessToken, int id) {
        Call<ResponseBody> call = apiInterface.deleteWallet("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else ((WalletActivity) context).finish();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getStats(String accessToken, int id, String dateFrom, String dateTo, OnStatsCallback callback) {
        Call<Map<String, Object>> call = apiInterface.getStats("Bearer " + accessToken, id, dateFrom, dateTo);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NotNull Call<Map<String, Object>> call, @NotNull Response<Map<String, Object>> response) {
                if (response.code() == 200)
                    callback.onStats(response.body());
                else if (response.code() == 400)
                    Toast.makeText(context, "Nieporpawne daty", Toast.LENGTH_LONG).show();
                else Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NotNull Call<Map<String, Object>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getMembersByInfix(WalletService.OnMemberSearchCallback callback, String accessToken, String infix) {
        Call<List<Member>> call = apiInterface.getMembersByInfix("Bearer " + accessToken, infix);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(@NotNull Call<List<Member>> call, @NotNull Response<List<Member>> response) {
                callback.onMembersList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Member>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getMembersByInfixInWallet(WalletService.OnMemberSearchCallback callback, String accessToken, int walletId, String infix) {
        Call<List<Member>> call = apiInterface.getMembersByInfixInWallet("Bearer " + accessToken, walletId, infix);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(@NotNull Call<List<Member>> call, @NotNull Response<List<Member>> response) {
                callback.onMembersList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Member>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void sendInvitationToUser(String accessToken, int id, String login) {
        Call<ResponseBody> call = apiInterface.sendInvitationToUser("Bearer " + accessToken, id, login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteMember(String accessToken, int id, String userLogin) {
        Call<ResponseBody> call = apiInterface.deleteMember("Bearer " + accessToken, id, userLogin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteCurrentMember(String accessToken, int id) {
        Call<ResponseBody> call = apiInterface.deleteCurrentMember("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else ((WalletActivity) context).finish();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
