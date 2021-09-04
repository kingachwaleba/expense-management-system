package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Category;
import com.example.mobile.model.Unit;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.WalletAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    static List<Category> categoriesWallet,  categoriesExpense;
    static List<Unit> productUnits;
    static List<String> periods;
    SessionManager session;
    RecyclerView walletRv;
    TextView helloTv;
    Button addNewWalletBtn;
    ValidationTableService validationTableService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTv = findViewById(R.id.hello_label);
        addNewWalletBtn = findViewById(R.id.add_new_wallet_btn);
        walletRv = findViewById(R.id.wallets_rv);

        walletRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        List<WalletCreate> walletItemsInit = new ArrayList<>();
        WalletAdapter walletAdapterInit = new WalletAdapter(MainActivity.this, walletItemsInit);
        walletRv.setAdapter(walletAdapterInit);

        validationTableService = new ValidationTableService(this);

        validationTableService.getWalletCategories(categories -> categoriesWallet = categories);

        validationTableService.getExpenseCategories(categories -> categoriesExpense = categories);

        validationTableService.getUnits(units -> productUnits = units);

        periods = new ArrayList<>();

        periods.add("brak");
        periods.add("dzienne");
        periods.add("tygodniowe");
        periods.add("miesięczne");
        periods.add("kwartalne");
        periods.add("roczne");

        addNewWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CreateWalletActivity.class);
            startActivity(i);
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        if (session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            String login = user.get(SessionManager.KEY_LOGIN);
            String hello = getResources().getString(R.string.hello) + " " + login + "!";
            helloTv.setText(hello);
            WalletService walletService = new WalletService(MainActivity.this, walletRv);
            walletService.getUserWallets(user.get(SessionManager.KEY_TOKEN));
        }
    }

    static public List<Category> getCategoriesWallet() {
        return categoriesWallet;
    }

    static public List<Category> getCategoriesExpense() {
        return categoriesExpense;
    }

    static public List<Unit> getProductUnits() {
        return productUnits;
    }

    public static List<String> getPeriods() {
        return periods;
    }
}