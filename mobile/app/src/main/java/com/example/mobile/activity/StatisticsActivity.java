package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.mobile.R;
import com.example.mobile.service.WalletService;
import java.io.IOException;

public class StatisticsActivity extends BaseActivity {

    String accessToken, walletName;
    int walletId;
    TextView walletNameTv;
    Button showStatisticsBtn, backBtn;
    WalletService walletService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        accessToken = getIntent().getStringExtra("accessToken");
        walletName = getIntent().getStringExtra("walletName");
        walletId = getIntent().getIntExtra("walletId", 0);

        walletNameTv = findViewById(R.id.name_tv);
        walletNameTv.setText(walletName);

        showStatisticsBtn = findViewById(R.id.show_btn);
        backBtn = findViewById(R.id.back_btn);

        walletService = new WalletService(this);

        showStatisticsBtn.setOnClickListener(v -> walletService.getStats(accessToken, walletId, "2021-01-01T00:00:00", "2021-11-10T23:59:59", response -> {
            /*try{
                System.out.println(response.string());
            } catch (IOException e){
                e.printStackTrace();
            }*/
            System.out.println(response);
        }));

        backBtn.setOnClickListener(v -> finish());
    }
}