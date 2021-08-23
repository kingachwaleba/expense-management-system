package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Account;
import com.example.mobile.model.Invitation;
import com.example.mobile.service.AccountService;
import com.example.mobile.service.adapter.InvitationAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    Button openNotificationBtn;
    Boolean ifOpenNotification;
    RecyclerView invitations_rv, warning_rv;
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

        invitations_rv = findViewById(R.id.notification_invitation_rv);

        invitations_rv.setLayoutManager(new LinearLayoutManager(this));
        List<Invitation> ins = new ArrayList<>();
        InvitationAdapter invitationAdapter = new InvitationAdapter(this, ins, accessToken);
        invitations_rv.setAdapter(invitationAdapter);


        ifOpenNotification = false;
        openNotificationBtn = findViewById(R.id.open_notification_btn);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setClipToOutline(true);

        loginTv = findViewById(R.id.login_tv);
        emailTv = findViewById(R.id.email_tv);
        numberOfWalletTv = findViewById(R.id.number_of_wallets_tv);
        balanceTv = findViewById(R.id.balance_tv);

        accountService.getAccount(new AccountService.OnAccountCallback() {
            @Override
            public void onMyAccount(Account account) {
                loginTv.setText(getResources().getString(R.string.login_string) + " " + account.getLogin());
                emailTv.setText(getResources().getString(R.string.email_string) + " " + account.getEmail());
                numberOfWalletTv.setText(getResources().getString(R.string.numer_of_wallets_string) + " " + account.getUserListCounter());
                //balanceTv.setText(getResources().getString(R.string.login_string) + " " + account.getLogin());
            }
        }, accessToken);

        openNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifOpenNotification = !ifOpenNotification;
                if(ifOpenNotification){
                    openNotificationBtn.setBackgroundResource(R.drawable.btn_list_opened);
                    accountService.getInvitations(invitations -> {
                        InvitationAdapter invitationAdapter1 = new InvitationAdapter(ProfileActivity.this, invitations, accessToken);
                        invitations_rv.setAdapter(invitationAdapter1);
                        invitationAdapter1.notifyDataSetChanged();
                    }, accessToken);

                } else {
                    openNotificationBtn.setBackgroundResource(R.drawable.btn_list_closed);
                    invitationAdapter.clear();
                    invitations_rv.setAdapter(invitationAdapter);
                    invitationAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}