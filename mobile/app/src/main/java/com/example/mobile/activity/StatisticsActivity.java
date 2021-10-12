package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mobile.R;
import com.example.mobile.service.WalletService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends BaseActivity {

    String accessToken, walletName;
    int walletId;
    TextView walletNameTv;
    Button showStatisticsBtn, backBtn;
    WalletService walletService;
    TextView biggestExpensesMembersTv, allExpensesTv;
    LinearLayout ll;

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
        biggestExpensesMembersTv = findViewById(R.id.biggest_expense_member_tv);
        allExpensesTv = findViewById(R.id.all_expense_tv);
        ll = findViewById(R.id.stats_l);

        walletService = new WalletService(this);

        showStatisticsBtn.setOnClickListener(v -> walletService.getStats(accessToken, walletId, "2021-01-01T00:00:00", "2021-10-11T23:59:59", response -> {
            System.out.println(response);
            HashMap<String, Double> statsList = new HashMap<>();
            String temp;

            for (Map.Entry<String,Object> entry : response.entrySet()){
                if(entry.getKey().equals("totalCost")){
                    allExpensesTv.setText(response.get("totalCost").toString());
                } else if(entry.getKey().equals("maxUsersList")){
                    List<String> lm= (List<String>) response.get("maxUsersList");
                    if(lm.size()>0){
                        String s = "";
                        for(int i = 0; i < lm.size(); i++)
                            s = lm.get(i) + " ";
                        biggestExpensesMembersTv.setText(s);
                    }
                } else if(entry.getKey().equals("maxExpensesValue")){
                    String temp2 = biggestExpensesMembersTv.getText().toString();
                    temp2 = temp2 + " " + entry.getValue();
                    biggestExpensesMembersTv.setText(temp2);
                }
                else statsList.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
            }

            for (Map.Entry<String,Double> entry : statsList.entrySet()){
                TextView t = new TextView(StatisticsActivity.this);
                temp = entry.getKey() + ": " + entry.getValue();
                t.setText(temp);
                t.setTextAppearance(R.style.simple_label_bold);
                t.setTextSize(18);
                ll.addView(t);
            }

        }));

        backBtn.setOnClickListener(v -> finish());
    }
}