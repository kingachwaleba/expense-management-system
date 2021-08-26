package com.example.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.model.Category;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.WalletService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditWalletActivity extends AppCompatActivity {

    EditText nameEt, descriptionEt;
    RadioGroup categoryRg;
    String accessToken;
    Category category;
    Button updateWalletBtn;
    WalletService walletService;
    int walletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        walletService = new WalletService(this);

        accessToken = getIntent().getStringExtra("accessToken");
        walletId = getIntent().getIntExtra("walletId",0);
        categoryRg = findViewById(R.id.category_RG);
        nameEt = findViewById(R.id.name_et);
        descriptionEt = findViewById(R.id.description_et);

        updateWalletBtn = findViewById(R.id.update_wallet_btn);

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getWalletCategories(categories -> {
            for(int i = 0; i < categories.size(); i++){
                RadioButton rdbtn = new RadioButton(EditWalletActivity.this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(categories.get(i).getName());
                rdbtn.setTextAppearance(R.style.simple_label);
                rdbtn.setTextSize(18);
                rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
                categoryRg.addView(rdbtn);
                if(i == 0) rdbtn.setChecked(true);
            }
        });

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            category = new Category(checkedId, radioText);
        });

        updateWalletBtn.setOnClickListener(v -> {
            String nameS = nameEt.getText().toString();
            WalletCreate walletCreate;
            if(validateName(nameS)){
                String descriptionS = descriptionEt.getText().toString();
                Map<String, String> map = new HashMap<>();
                map.put("name", nameS);
                map.put("description", descriptionS);
                map.put("walletCategory", category.getName());
                walletService.updateWallet(accessToken, walletId, map);
                finish();
            } else Toast.makeText(EditWalletActivity.this, "Brak nazwy portfela", Toast.LENGTH_LONG).show();
        });
    }

    public boolean validateName(String s){
        return s.length() > 0;
    }
}



