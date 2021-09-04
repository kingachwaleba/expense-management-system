package com.example.mobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(getApplicationContext());
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
        if(id == R.id.goToProfile){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.goToWallets){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}