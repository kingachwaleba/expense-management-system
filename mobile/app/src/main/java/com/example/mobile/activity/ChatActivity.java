package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Message;
import com.example.mobile.service.ChatService;

public class ChatActivity extends BaseActivity {

    EditText contentEt;
    TextView nameWallet;
    Button sendMessageBtn;
    RecyclerView messageRv;
    String accessToken, walletName;
    int walletId;
    ChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        walletId = getIntent().getIntExtra("walletId", 0);
        accessToken = getIntent().getStringExtra("accessToken");
        walletName = getIntent().getStringExtra("walletName");

        chatService= new ChatService(this, walletId);

        messageRv = findViewById(R.id.message_rv);
        contentEt = findViewById(R.id.message_et);
        nameWallet = findViewById(R.id.name_tv);
        sendMessageBtn = findViewById(R.id.send_message_btn);

        nameWallet.setText(walletName);

        chatService.getMessages(accessToken);

        sendMessageBtn.setOnClickListener(v -> {
            if(!contentEt.getText().toString().equals("")){
                chatService.sendMessage(accessToken, walletId, new Message(contentEt.getText().toString()));
                contentEt.setText("");
            }
        });

    }

}