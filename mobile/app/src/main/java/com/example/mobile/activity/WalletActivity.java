package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    Boolean showMembersControl;
    String accesToken;
    String TAG = "MEMBERS_FRAGMENT";

    TextView walletNameTv, descriptionTv, ownerTv, numberOfMembersTv;
    Button showMembersBtn, addMemberBtn, editWalletBtn;
    int categoryId;

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
        showMembersControl = false;

        walletNameTv = findViewById(R.id.name_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ownerTv = findViewById(R.id.owner_tv);
        numberOfMembersTv = findViewById(R.id.number_of_members_tv);
        showMembersBtn = findViewById(R.id.show_members_btn);
        addMemberBtn = findViewById(R.id.add_member_btn);
        showMembersBtn.setBackgroundResource(R.drawable.btn_list_closed);
        editWalletBtn = findViewById(R.id.edit_wallet_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            walletNameTv.setText(walletModel.getName());
            if(walletModel.getDescription()!=null)
                descriptionTv.setText(getResources().getString(R.string.description_label) + " " + walletModel.getDescription());
            ownerTv.setText(getResources().getString(R.string.owner_label) + " " + walletModel.getOwner());
            numberOfMembersTv.setText(getResources().getString(R.string.number_of_members_label) + " " + walletModel.getUserListCounter());
            showMembersBtn.setOnClickListener(v -> {
                if (!showMembersControl) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("members", (ArrayList<Member>)walletModel.getUserList());
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, MembersFragment.class, bundle, TAG)
                                .commit();
                        showMembersBtn.setBackgroundResource(R.drawable.btn_list_opened);
                        showMembersControl = true;
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if(fragment != null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    showMembersBtn.setBackgroundResource(R.drawable.btn_list_closed);
                    showMembersControl = false;
                }
            });
        }, accesToken, id);

        addMemberBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, AddMemberActivity.class);
            intent.putExtra("name",walletNameTv.getText().toString());
            intent.putExtra("walletId",id);
            intent.putExtra("accessToken", accesToken);
            startActivity(intent);
        });

        editWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, EditWalletActivity.class);
                intent.putExtra("accessToken", accesToken);
                intent.putExtra("walletId",id);
                startActivity(intent);
            }
        });
    }

}