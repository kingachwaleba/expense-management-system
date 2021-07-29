package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobile.config.SessionManager;
import com.example.mobile.model.LoginForm;
import com.example.mobile.model.Wallet;
import com.example.mobile.service.UserService;
import com.example.mobile.service.WalletAdapter;
import com.example.mobile.service.WalletService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    Button logout_btn;
    TextView login_tv;
    RecyclerView wallet_rv;
    List<Wallet> myWallets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        logout_btn = findViewById(R.id.logout);
        login_tv = findViewById(R.id.login_tv);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

        wallet_rv = (RecyclerView) findViewById(R.id.wallet_rv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
//        String login = user.get(SessionManager.KEY_LOGIN);
//
//        login_tv.setText(login);

        WalletService walletService = new WalletService(MainActivity.this);

        walletService.getAll(user.get(SessionManager.KEY_TOKEN), new WalletService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                System.out.println("dsdsdsEEEd");
            }

            @Override
            public void onResponse(List<Wallet> wallets) {
                WalletAdapter walletAdapter = new WalletAdapter(MainActivity.this,wallets);
                wallet_rv.setAdapter(walletAdapter);
                wallet_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
    }
}