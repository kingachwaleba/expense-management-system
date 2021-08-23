package com.example.mobile.service;

import android.content.Context;
import android.widget.Toast;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.Account;
import com.example.mobile.model.Invitation;
import org.jetbrains.annotations.NotNull;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountService {
    Context context;
    ApiInterface apiInterface;

    public AccountService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnAccountCallback{
        void onMyAccount(Account account);
    }

    public interface OnInvitationCallback{
        void onAllInvitations(List<Invitation> invitations);
    }


    public void getAccount(OnAccountCallback callback, String accessToken){
        Call<Account> call = apiInterface.getAccount("Bearer " + accessToken);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NotNull Call<Account> call, @NotNull Response<Account> response) {
                callback.onMyAccount(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Account> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getInvitations(OnInvitationCallback callback, String accessToken){
        Call<List<Invitation>> call = apiInterface.getInvitations("Bearer " + accessToken);
        call.enqueue(new Callback<List<Invitation>>() {
            @Override
            public void onResponse(@NotNull Call<List<Invitation>> call, @NotNull Response<List<Invitation>> response) {
                callback.onAllInvitations(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Invitation>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void manageInvitation(String accessToken, int id, Boolean flag){
        Call<ResponseBody> call = apiInterface.manageInvitation("Bearer " + accessToken, id, flag);
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
}
