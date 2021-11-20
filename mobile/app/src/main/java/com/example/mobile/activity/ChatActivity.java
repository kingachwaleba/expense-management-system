package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Message;
import com.example.mobile.service.ChatService;
import com.example.mobile.service.adapter.ChatAdapter;
import java.time.LocalDateTime;
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
    String date, userLogin;
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

        date = LocalDateTime.now().toString().substring(0,19);

        messageRv = findViewById(R.id.message_rv);
        chatAdapter = new ChatAdapter(this, allMessages, userLogin, accessToken);

        contentEt = findViewById(R.id.message_et);
        nameWallet = findViewById(R.id.name_tv);
        sendMessageBtn = findViewById(R.id.send_message_btn);

        nameWallet.setText(walletName);

        messageRv.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        List<Message> messagesInit = new ArrayList<>();
        ChatAdapter chatAdapterInit = new ChatAdapter(ChatActivity.this, messagesInit, userLogin, accessToken);
        messageRv.setAdapter(chatAdapterInit);

        chatService= new ChatService(this, walletId, messageRv, userLogin);

        chatService.getMessages(accessToken, date);


  /*      chatService.getMessages(accessToken, date, new ChatService.OnMessagesCallback() {
            @Override
            public void onMessages(List<Message> messages) {
                allMessages = messages;

                chatAdapter.notifyDataSetChanged();
                messageRv.setAdapter(chatAdapter);

                for(int i = 0; i < allMessages.size(); i++)
                    System.out.println(allMessages.get(i).getContent());
            }
        });*/



        sendMessageBtn.setOnClickListener(v -> {
            if(!contentEt.getText().toString().equals("")){
                chatService.sendMessage(accessToken, walletId, new Message(contentEt.getText().toString()));
                contentEt.setText("");
            }
        });

    }

}