package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.service.WalletService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsActivity extends BaseActivity {

    TextView walletNameTv, biggestExpensesMembersTv, allExpensesTv;
    Button showStatisticsBtn, backBtn;
    LinearLayout ll;
    Spinner day1, day2, month1, month2, year1, year2;

    WalletService walletService;
    String accessToken, walletName;
    int walletId;

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

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        month1 = findViewById(R.id.month1);
        month2 = findViewById(R.id.month2);
        year1 = findViewById(R.id.year1);
        year2 = findViewById(R.id.year2);

        ArrayList<String> yearsList = new ArrayList<>();
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2021; i <= current_year; i++) {
            yearsList.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, yearsList);
        yearsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        year1.setAdapter(yearsAdapter);
        year2.setAdapter(yearsAdapter);

        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            monthList.add(String.format(Locale.getDefault(), "%02d", i));
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, monthList);
        monthAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        month1.setAdapter(monthAdapter);
        month2.setAdapter(monthAdapter);

        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++)
            dayList.add(String.format(Locale.getDefault(), "%02d", i));
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, dayList);
        dayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        day1.setAdapter(dayAdapter);
        day2.setAdapter(dayAdapter);

        walletService = new WalletService(this);

        showStatisticsBtn.setOnClickListener(v -> {

            String dateFrom, dateTo;
            dateFrom = year1.getSelectedItem().toString() + "-" + month1.getSelectedItem().toString() + "-" + day1.getSelectedItem().toString() + "T00:00:00";
            dateTo = year2.getSelectedItem().toString() + "-" + month2.getSelectedItem().toString() + "-" + day2.getSelectedItem().toString() + "T23:59:59";

            if (validateDate(dateFrom, dateTo)) {
                ll.removeAllViews();
                biggestExpensesMembersTv.setText("");
                allExpensesTv.setText("");
                walletService.getStats(accessToken, walletId, dateFrom, dateTo, response -> {
                    HashMap<String, Double> statsList = new HashMap<>();
                    String temp;
                    for (Map.Entry<String, Object> entry : response.entrySet()) {
                        switch (entry.getKey()) {
                            case "totalCost":
                                allExpensesTv.setText(String.valueOf(response.get("totalCost")));
                                break;
                            case "maxUsersList":
                                List<String> lm = (List<String>) response.get("maxUsersList");
                                if (lm != null) {
                                    String s = "";
                                    for (int i = 0; i < lm.size(); i++)
                                        s = lm.get(i) + " ";
                                    biggestExpensesMembersTv.setText(s);
                                }
                                break;
                            case "maxExpensesValue":
                                String temp2 = biggestExpensesMembersTv.getText().toString();
                                temp2 = temp2 + " " + entry.getValue();
                                biggestExpensesMembersTv.setText(temp2);
                                break;
                            default:
                                statsList.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
                                break;
                        }
                    }

                    for (Map.Entry<String, Double> entry : statsList.entrySet()) {
                        TextView t = new TextView(StatisticsActivity.this);
                        temp = entry.getKey() + ": \t" + entry.getValue();
                        t.setText(temp);
                        t.setTextAppearance(R.style.simple_label_bold);
                        t.setTextSize(18);
                        ll.addView(t);
                    }
                });
            } else Toast.makeText(this, "Podaj poprawne daty", Toast.LENGTH_LONG).show();
        });

        backBtn.setOnClickListener(v -> finish());
    }

    boolean validateDate(String dateFrom, String dateTo) {
        LocalDateTime from;
        LocalDateTime to;
        try {
            from = LocalDateTime.parse(dateFrom);
            to = LocalDateTime.parse(dateTo);
        } catch (DateTimeException dateTimeException) {
            return false;
        }

        return !to.isBefore(from) && !from.isAfter(LocalDateTime.now())
                && !to.isAfter(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
    }
}