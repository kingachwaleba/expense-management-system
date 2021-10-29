package com.example.mobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.CircleTransform;
import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;


public class BaseActivity extends AppCompatActivity {

    Menu menu;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        String profile = session.getUserDetails().get(SessionManager.KEY_IMAGE_PATH_SERVER);

        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), new CircleTransform().getCroppedBitmap(bitmap));
                menu.getItem(0).setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };
        if (profile != null) {
            ImageHelper.downloadImage((picasso, urlBuilder) ->
                            picasso.load(String.valueOf(urlBuilder)).into(mTarget), getApplicationContext(),
                    session.getUserDetails().get(SessionManager.KEY_TOKEN), profile);
        } else Picasso.with(this).load(R.drawable.profile_picture_placeholder).into(mTarget);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            session.logoutUser();
            finish();
            return true;
        }
        if (id == R.id.goToProfile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.goToWallets) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
