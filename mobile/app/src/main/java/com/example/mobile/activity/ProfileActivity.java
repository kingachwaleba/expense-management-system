package com.example.mobile.activity;

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

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    Button openNotificationBtn;
    Boolean ifOpenNotification;
    RecyclerView notificationInvitationRv, notificationWarningRv, notificationMessageRv;
    SessionManager session;
    String accessToken;
    AccountService accountService;
    TextView loginTv, emailTv, numberOfWalletTv, balanceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        accountService = new AccountService(this);

        notificationInvitationRv = findViewById(R.id.notification_invitation_rv);

        notificationInvitationRv.setLayoutManager(new LinearLayoutManager(this));
        List<Invitation> invitationsInit = new ArrayList<>();
        InvitationAdapter invitationAdapterInit = new InvitationAdapter(this, invitationsInit, accessToken);
        notificationInvitationRv.setAdapter(invitationAdapterInit);


        ifOpenNotification = false;
        openNotificationBtn = findViewById(R.id.open_notification_btn);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setClipToOutline(true);

        loginTv = findViewById(R.id.login_tv);
        emailTv = findViewById(R.id.email_tv);
        numberOfWalletTv = findViewById(R.id.number_of_wallets_tv);
        balanceTv = findViewById(R.id.balance_tv);

        accountService.getAccount(account -> {
            loginTv.setText(getResources().getString(R.string.login_label) + " " + account.getLogin());
            emailTv.setText(getResources().getString(R.string.email_label) + " " + account.getEmail());
            numberOfWalletTv.setText(getResources().getString(R.string.numer_of_wallets_label) + " " + account.getUserListCounter());
            //balanceTv.setText(getResources().getString(R.string.login_string) + " " + account.getLogin());
        }, accessToken);

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
    }
}