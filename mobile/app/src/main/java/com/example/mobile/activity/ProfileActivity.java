package com.example.mobile.activity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Invitation;
import com.example.mobile.model.Message;
import com.example.mobile.model.User;
import com.example.mobile.service.AccountService;
import com.example.mobile.service.adapter.InvitationAdapter;
import com.example.mobile.service.adapter.WarningAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    ImageView profileImage;
    Button editProfileBtn;
    ImageButton openNotificationBtn;
    Boolean ifOpenNotification;
    RecyclerView notificationInvitationRv, notificationWarningRv;
    SessionManager session;
    String accessToken;
    AccountService accountService;
    TextView loginTv, emailTv, numberOfWalletTv, balanceTv, goToStatuteTv;
    User user;
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
            session.setKeyImagePathServer(account.getImage());
            loginTv.setText(loginText);
            emailTv.setText(emailText);
            numberOfWalletTv.setText(numberOfWalletText);
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
            }
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
            ApiInterface apiInterface = new ApiClient().getService();
            String path = session.getUserDetails().get(SessionManager.KEY_IMAGE_PATH_SERVER);
            //creating a call and calling the upload image method
            Call<ResponseBody> call = apiInterface.download("Bearer " + accessToken, path);

            //finally performing the call
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   /* try {
                        downloadImage(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Coś poszło nie tak" , Toast.LENGTH_LONG).show();
                }
            });


    }

    private void downloadImage(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "journaldev-image-downloaded.jpg");
        OutputStream outputStream = new FileOutputStream(outputFile);
        long total = 0;
        boolean downloadComplete = false;
        //int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));

        while ((count = inputStream.read(data)) != -1) {

            total += count;
            int progress = (int) ((double) (total * 100) / (double) fileSize);


          //  updateNotification(progress);
            outputStream.write(data, 0, count);
            downloadComplete = true;
        }
       // onDownloadComplete(downloadComplete);
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }
}