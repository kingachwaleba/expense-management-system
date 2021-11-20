package com.example.mobile.service;

import android.content.Context;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.Message;
import com.example.mobile.service.adapter.ChatAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatService {
    Context context;
    ApiInterface apiInterface;
    RecyclerView chatRv;
    int walletId;
    String userLogin;

    public ChatService(Context context, int id, RecyclerView rv, String userLogin) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
        this.walletId = id;
        this.chatRv = rv;
        this.userLogin = userLogin;
    }

    public interface OnMessagesCallback {
        void onMessages(List<Message> messages);
    }

    public void getMessages(String accessToken, String date) {
        Call<List<Message>> call = apiInterface.getMessages("Bearer " + accessToken, walletId, date);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NotNull Call<List<Message>> call, @NotNull Response<List<Message>> response) {
                if(response.isSuccessful()){
                    List<Message> messages = response.body();
                    ChatAdapter chatAdapter = new ChatAdapter(context, messages, userLogin, accessToken);
                    chatRv.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Message>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void sendMessage(String accessToken, int walletId, Message message) {
        Call<ResponseBody> call = apiInterface.sendMessage("Bearer " + accessToken, walletId, message);
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

}
