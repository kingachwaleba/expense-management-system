package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Category;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.example.mobile.service.adapter.SearchUserAdapter;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.WalletService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateWalletActivity extends AppCompatActivity {

    SessionManager session;
    WalletService walletService;

    WalletCreate walletCreate;
    Category category;
    String accesToken;

    RadioGroup category_RG;
    EditText name, description, search;
    Button create;
    RecyclerView add_user_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        walletService = new WalletService(getParent());

        session = new SessionManager(getApplicationContext());

        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        name = findViewById(R.id.nameEdit);
        description = findViewById(R.id.descriptionEdit);
        search = findViewById(R.id.memberEdit);
        create = findViewById(R.id.create_wallet_btn);
        add_user_rv = findViewById(R.id.members_search_rv);
        category_RG = findViewById(R.id.category_RG);

        add_user_rv.setLayoutManager(new LinearLayoutManager(this));
        List<String> members = new ArrayList<>();
        SearchUserAdapter searchUserAdapter = new SearchUserAdapter(this, members);
        add_user_rv.setAdapter(searchUserAdapter);

        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(search.getText().length()>0){
                    walletService.getMembersByInfix(members1 -> {
                        SearchUserAdapter searchUserAdapter1 = new SearchUserAdapter(CreateWalletActivity.this, members1);
                        add_user_rv.setAdapter(searchUserAdapter1);
                        searchUserAdapter1.notifyDataSetChanged();
                    }, search.getText().toString());
                }
            }
        });

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getCategories(categories -> {
            for(int i = 0; i < categories.size(); i++){
                RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(categories.get(i).getName());
                rdbtn.setTextAppearance(R.style.label);
                rdbtn.setTextSize(18);
                rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
                category_RG.addView(rdbtn);
                if(i == 0) rdbtn.setChecked(true);
            }
        });

        category_RG.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            category = new Category(checkedId, radioText);
        });

        create.setOnClickListener(v -> {
            String nameS = name.getText().toString();
            if(validateName(nameS)){
                String descriptionS = description.getText().toString();
                walletCreate = new WalletCreate(nameS, descriptionS, category);
                WalletHolder walletHolder = new WalletHolder(walletCreate, searchUserAdapter.getSelectedUser());
                walletService.createWallet(accesToken, walletHolder);
            } else Toast.makeText(CreateWalletActivity.this, "Brak nazwy portfela", Toast.LENGTH_LONG).show();
        });
    }

    public boolean validateName(String s){
        return s.length() > 0;
    }
}