package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.mobile.config.SessionManager;
import com.example.mobile.service.WalletService;
import com.example.mobile.model.WalletModel;

public class WalletActivity extends AppCompatActivity {

    int id;
    SessionManager session;
    String accesToken;
    TextView walletName_tv, description_tv, owner_tv, number_of_members_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        session = new SessionManager(getApplicationContext());
        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        walletName_tv = findViewById(R.id.walletName_tv);
        description_tv = findViewById(R.id.description_tv);
        owner_tv = findViewById(R.id.owner_tv);
        number_of_members_tv = findViewById(R.id.number_of_members_tv);
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
            number_of_members_tv.setText(getResources().getString(R.string.number_of_members) + " " + String.valueOf(walletModel.getUserListCounter()));

            for(int i = 0; i < walletModel.getUserListCounter();i++){
                System.out.println(walletModel.getUserList().get(i).getLogin());
            }

        }, accesToken, id);
    }

}