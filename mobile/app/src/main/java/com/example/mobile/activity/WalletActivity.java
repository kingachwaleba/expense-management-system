package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.mobile.fragment.MembersFragment;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;
import java.util.ArrayList;
import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    SessionManager session;

    int id;
    Boolean show_members_control;
    String accesToken;
    String TAG = "MEMBERS_FRAGMENT";

    TextView walletName_tv, description_tv, owner_tv, number_of_members_tv;
    Button show_members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(getApplicationContext());

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        show_members_control = false;

        walletName_tv = findViewById(R.id.walletName_tv);
        description_tv = findViewById(R.id.description_tv);
        owner_tv = findViewById(R.id.owner_tv);
        number_of_members_tv = findViewById(R.id.number_of_members_tv);
        show_members = findViewById(R.id.show_members);

        show_members.setBackgroundResource(R.drawable.btn_list_closed);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            walletName_tv.setText(walletModel.getName());
            if(walletModel.getDescription()!=null)
                description_tv.setText(getResources().getString(R.string.description) + " " + walletModel.getDescription());
            owner_tv.setText(getResources().getString(R.string.owner) + " " + walletModel.getOwner());
            number_of_members_tv.setText(getResources().getString(R.string.number_of_members) + " " + walletModel.getUserListCounter());

            show_members.setOnClickListener(v -> {
                if (!show_members_control) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("members", (ArrayList<Member>)walletModel.getUserList());
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, MembersFragment.class, bundle, TAG)
                                .commit();
                        show_members.setBackgroundResource(R.drawable.btn_list_opened);
                        show_members_control = true;
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if(fragment != null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    show_members.setBackgroundResource(R.drawable.btn_list_closed);
                    show_members_control = false;
                }
            });
        }, accesToken, id);
    }

}