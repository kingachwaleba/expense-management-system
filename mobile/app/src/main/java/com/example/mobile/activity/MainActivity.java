package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.WalletItem;
import com.example.mobile.service.adapter.WalletAdapter;
import com.example.mobile.service.WalletService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SessionManager session;

    RecyclerView wallet_rv;
    TextView hello_tv;
    Button add_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        hello_tv = findViewById(R.id.hello_label);
        add_wallet = findViewById(R.id.go_to_create_wallet_btn);
        wallet_rv = findViewById(R.id.wallet_rv);

        wallet_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        List<WalletItem> walletItems = new ArrayList<>();
        WalletAdapter walletAdapter = new WalletAdapter(MainActivity.this, walletItems);
        wallet_rv.setAdapter(walletAdapter);

        add_wallet.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CreateWalletActivity.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if(id == R.id.logout){
            session.logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        if (session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            String login = user.get(SessionManager.KEY_LOGIN);

            hello_tv.setText(getResources().getString(R.string.hello) + " " + login + "!");

            WalletService walletService = new WalletService(MainActivity.this, wallet_rv);
            walletService.getUserWallets(user.get(SessionManager.KEY_TOKEN));
        }
    }
}