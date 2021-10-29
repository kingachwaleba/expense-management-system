package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Invitation;
import com.example.mobile.model.Message;
import com.example.mobile.model.User;
import com.example.mobile.service.AccountService;
import com.example.mobile.service.adapter.InvitationAdapter;
import com.example.mobile.service.adapter.WarningAdapter;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity {

    ImageView profileImage;
    Button editProfileBtn;
    ImageButton openNotificationBtn;
    TextView loginTv, emailTv, numberOfWalletTv, balanceTv, goToStatuteTv;
    RecyclerView notificationInvitationRv, notificationWarningRv;

    SessionManager session;
    String accessToken;
    AccountService accountService;
    User user;
    Boolean ifOpenNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        accountService = new AccountService(this);

        notificationInvitationRv = findViewById(R.id.notification_invitation_rv);
        notificationWarningRv = findViewById(R.id.notification_warning_rv);

        notificationInvitationRv.setLayoutManager(new LinearLayoutManager(this));
        notificationWarningRv.setLayoutManager(new LinearLayoutManager(this));

        List<Invitation> invitationsInit = new ArrayList<>();
        List<Message> warningsInit = new ArrayList<>();

        InvitationAdapter invitationAdapterInit = new InvitationAdapter(this, invitationsInit, accessToken);
        WarningAdapter warningAdapterInit = new WarningAdapter(this, warningsInit, accessToken);

        notificationInvitationRv.setAdapter(invitationAdapterInit);
        notificationWarningRv.setAdapter(warningAdapterInit);

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
            user = account;
            String loginText = getResources().getString(R.string.login_label) + " " + account.getLogin();
            String emailText = getResources().getString(R.string.email_label) + " " + account.getEmail();
            String numberOfWalletText = getResources().getString(R.string.numer_of_wallets_label) + " " + account.getWalletsNumber();
            String balanceText = getResources().getString(R.string.saldo_label) + " " + account.getUserBalance();
            session.setKeyImagePathServer(account.getImage());
            if(account.getImage()!=null){
                ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into(profileImage), getApplicationContext(), accessToken, account.getImage());
            }

            loginTv.setText(loginText);
            emailTv.setText(emailText);
            numberOfWalletTv.setText(numberOfWalletText);
            balanceTv.setText(balanceText);
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
                accountService.getDebtNotification(messages -> {
                    WarningAdapter warningAdapter = new WarningAdapter(ProfileActivity.this, messages, accessToken);
                    notificationWarningRv.setAdapter(warningAdapter);
                    warningAdapter.notifyDataSetChanged();
                }, accessToken);

            } else {
                openNotificationBtn.setBackgroundResource(R.drawable.btn_list_closed);
                invitationAdapterInit.clear();
                notificationInvitationRv.setAdapter(invitationAdapterInit);
                invitationAdapterInit.notifyDataSetChanged();
                warningAdapterInit.clear();
                notificationWarningRv.setAdapter(warningAdapterInit);
                warningAdapterInit.notifyDataSetChanged();
            }
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

    }
}