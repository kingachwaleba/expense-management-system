package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile.R;

public class ChatActivity extends AppCompatActivity {

    String accessToken;
    int walletId;
    EditText contentEt;
    Button sendMessageBtn;
    RecyclerView messageRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        walletId = getIntent().getIntExtra("walletId", 0);
        accessToken = getIntent().getStringExtra("accessToken");

        messageRv = findViewById(R.id.message_rv);
        contentEt = findViewById(R.id.message_et);
        sendMessageBtn = findViewById(R.id.send_message_btn);

    }
}