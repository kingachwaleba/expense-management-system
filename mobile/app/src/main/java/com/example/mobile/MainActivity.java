package com.example.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Wallet;
import com.example.mobile.service.WalletAdapter;
import com.example.mobile.service.WalletService;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    RecyclerView wallet_rv;
    TextView hello_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        hello_tv = findViewById(R.id.hello_label);
        wallet_rv = (RecyclerView) findViewById(R.id.wallet_rv);
        wallet_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // get current user login
            String login = user.get(SessionManager.KEY_LOGIN);
            hello_tv.setText(getResources().getString(R.string.hello) + " " + login + "!");
            WalletService walletService = new WalletService(MainActivity.this);

            walletService.getAll(user.get(SessionManager.KEY_TOKEN), new WalletService.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Log.d("Error", message);
                }

                @Override
                public void onResponse(List<Wallet> wallets) {
                    WalletAdapter walletAdapter = new WalletAdapter(MainActivity.this,wallets);
                    wallet_rv.setAdapter(walletAdapter);
                }
            });
        }
    }
}