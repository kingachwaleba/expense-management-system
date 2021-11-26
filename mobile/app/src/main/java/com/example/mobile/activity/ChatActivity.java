package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Message;
import com.example.mobile.service.ChatService;
import com.example.mobile.service.adapter.ChatAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    EditText contentEt;
    TextView nameWallet;
    Button sendMessageBtn;
    RecyclerView messageRv;
    String accessToken, walletName;
    int walletId;
    ChatService chatService;
    String userLogin;
    List<Message> allMessages;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        walletId = getIntent().getIntExtra("walletId", 0);
        accessToken = getIntent().getStringExtra("accessToken");
        walletName = getIntent().getStringExtra("walletName");
        userLogin = getIntent().getStringExtra("login");

        messageRv = findViewById(R.id.message_rv);
        chatAdapter = new ChatAdapter(this, allMessages, userLogin, accessToken);

        contentEt = findViewById(R.id.message_et);
        nameWallet = findViewById(R.id.name_tv);
        sendMessageBtn = findViewById(R.id.send_message_btn);

        nameWallet.setText(walletName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        messageRv.setLayoutManager(layoutManager);
        List<Message> messagesInit = new ArrayList<>();
        ChatAdapter chatAdapterInit = new ChatAdapter(ChatActivity.this, messagesInit, userLogin, accessToken);
        messageRv.setAdapter(chatAdapterInit);

        chatService= new ChatService(this, walletId, messageRv, userLogin);

        chatService.getMessages(accessToken);

        sendMessageBtn.setOnClickListener(v -> {
            String contentS = contentEt.getText().toString();
            if(!contentS.equals("") && contentS.length()<256){
                chatService.sendMessage(accessToken, walletId, new Message(contentS));
                contentEt.setText("");
            } else {
                Toast.makeText(this, "Wiadomość musi mieć od 1 do 255 znaków",
                        Toast.LENGTH_SHORT).show();
       }});

      //  contentEt.setOnClickListener(view -> messageRv.postDelayed(() -> messageRv.scrollToPosition(messageRv.getAdapter().getItemCount() - 1), 500));

        messageRv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(isRecyclerViewAtTop()) {
                    ChatAdapter chatAdapter1 = (ChatAdapter) messageRv.getAdapter();
                    if(chatAdapter1!=null){
                        List<Message> oldMessage = chatAdapter1.getmMessage();
                        chatService.getOldMessages(accessToken,oldMessage.get(0).getDate(), oldMessage);
                    }
                }
            }
        });
    }

    private boolean isRecyclerViewAtTop()   {
        if(messageRv.getChildCount() == 0)
            return true;
        return messageRv.getChildAt(0).getTop() == 0;
    }
}