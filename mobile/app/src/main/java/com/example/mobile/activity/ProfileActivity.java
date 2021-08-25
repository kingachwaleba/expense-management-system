package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Invitation;
import com.example.mobile.service.AccountService;
import com.example.mobile.service.adapter.InvitationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    Button openNotificationBtn, editProfileBtn;
    Boolean ifOpenNotification;
    RecyclerView notificationInvitationRv; // notificationWarningRv, notificationMessageRv;
    SessionManager session;
    String accessToken;
    AccountService accountService;
    TextView loginTv, emailTv, numberOfWalletTv, balanceTv, goToStatuteTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(this);
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        accountService = new AccountService(this);

        notificationInvitationRv = findViewById(R.id.notification_invitation_rv);
       /* notificationWarningRv = findViewById(R.id.notification_warning_rv);
        notificationMessageRv = findViewById(R.id.notification_message_rv);*/

        notificationInvitationRv.setLayoutManager(new LinearLayoutManager(this));
       /* notificationWarningRv.setLayoutManager(new LinearLayoutManager(this));
        notificationMessageRv.setLayoutManager(new LinearLayoutManager(this));*/

        List<Invitation> invitationsInit = new ArrayList<>();
       /* List<Warning> warningsInit = new ArrayList<>();
        List<MessageNotification> messageNotificationsInit = new ArrayList<>();*/

        InvitationAdapter invitationAdapterInit = new InvitationAdapter(this, invitationsInit, accessToken);
       /* WarningAdapter warningAdapterInit = new WarningAdapter(this, warningsInit);
        MessageNotificationAdapter messageNotificationAdapterInit = new MessageNotificationAdapter(this, messageNotificationsInit);*/

        notificationInvitationRv.setAdapter(invitationAdapterInit);
        /*notificationWarningRv.setAdapter(warningAdapterInit);
        notificationMessageRv.setAdapter(messageNotificationAdapterInit);*/

        ifOpenNotification = false;
        openNotificationBtn = findViewById(R.id.open_notification_btn);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setClipToOutline(true);

        loginTv = findViewById(R.id.login_tv);
        emailTv = findViewById(R.id.email_tv);
        numberOfWalletTv = findViewById(R.id.number_of_wallets_tv);
        balanceTv = findViewById(R.id.balance_tv);
        goToStatuteTv = findViewById(R.id.go_to_statue_tv);
        editProfileBtn = findViewById(R.id.edit_profile_btn);

        accountService.getAccount(account -> {
            loginTv.setText(getResources().getString(R.string.login_label) + " " + account.getLogin());
            emailTv.setText(getResources().getString(R.string.email_label) + " " + account.getEmail());
            numberOfWalletTv.setText(getResources().getString(R.string.numer_of_wallets_label) + " " + account.getWalletsNumber());
            //balanceTv.setText(getResources().getString(R.string.login_string) + " " + account.getLogin());
        }, accessToken);

        goToStatuteTv.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, StatueActivity.class);
            startActivity(intent);
        });

        openNotificationBtn.setOnClickListener(v -> {
            ifOpenNotification = !ifOpenNotification;
            if(ifOpenNotification){
                openNotificationBtn.setBackgroundResource(R.drawable.btn_list_opened);
                accountService.getInvitations(invitations -> {
                    InvitationAdapter invitationAdapter = new InvitationAdapter(ProfileActivity.this, invitations, accessToken);
                    notificationInvitationRv.setAdapter(invitationAdapter);
                    invitationAdapter.notifyDataSetChanged();
                }, accessToken);

            } else {
                openNotificationBtn.setBackgroundResource(R.drawable.btn_list_closed);
                invitationAdapterInit.clear();
                notificationInvitationRv.setAdapter(invitationAdapterInit);
                invitationAdapterInit.notifyDataSetChanged();
            }
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
}