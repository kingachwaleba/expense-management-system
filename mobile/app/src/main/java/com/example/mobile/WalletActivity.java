package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mobile.config.SessionManager;
import com.example.mobile.service.WalletService;

public class WalletActivity extends AppCompatActivity {

    int id;
    SessionManager session;
    String accesToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        session = new SessionManager(getApplicationContext());
        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(accesToken, id);
    }
}