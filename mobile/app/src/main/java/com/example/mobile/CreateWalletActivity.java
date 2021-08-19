package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Category;
import com.example.mobile.model.Member;
import com.example.mobile.model.Wallet;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.example.mobile.service.SearchUserAdapter;
import com.example.mobile.service.UserService;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.WalletService;

import java.util.ArrayList;
import java.util.List;

public class CreateWalletActivity extends AppCompatActivity {

    RadioGroup category_RG;
    EditText name, description, search;
    List<String> addUser;
    Button create;
    RecyclerView add_user_rv;
    WalletCreate walletCreate;
    Category category;
    List<String> userList;
    SessionManager session;
    String accesToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(getApplicationContext());
        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        name = findViewById(R.id.nameEdit);
        description = findViewById(R.id.descriptionEdit);
        search = findViewById(R.id.memberEdit);
        create = findViewById(R.id.create_wallet_btn);
        add_user_rv = findViewById(R.id.members_search_rv);

        add_user_rv = findViewById(R.id.members_search_rv);
        add_user_rv.setLayoutManager(new LinearLayoutManager(this));
        List<String> members = new ArrayList<>();
        SearchUserAdapter searchUserAdapter = new SearchUserAdapter(this, members);
        add_user_rv.setAdapter(searchUserAdapter);

        category_RG = findViewById(R.id.category_RG);

        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                WalletService walletService = new WalletService(getParent());
                if(!search.getText().toString().equals("")){
                    walletService.getMembersByInfix(new WalletService.OnMemberSearchCallback() {
                        @Override
                        public void onMembersList(List<String> members) {
                            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(CreateWalletActivity.this, members);
                            add_user_rv.setAdapter(searchUserAdapter);
                            searchUserAdapter.notifyDataSetChanged();
                        }
                    }, search.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getCategories(new ValidationTableService.OnValidationTable() {
            @Override
            public void onCategories(List<Category> categories) {
                for(int i = 0; i < categories.size(); i++){
                    RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(categories.get(i).getName());
                    rdbtn.setTextAppearance(R.style.label);
                    rdbtn.setTextSize(18);
                    rdbtn.setButtonDrawable(R.drawable.radio_button);
                    category_RG.addView(rdbtn);
                    if(i == 0) rdbtn.setChecked(true);
                }
            }
        });

        category_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb=findViewById(checkedId);
                String radioText = rb.getText().toString();
                category = new Category(checkedId, radioText);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameS = name.getText().toString();
                String descriptionS = description.getText().toString();
                walletCreate = new WalletCreate(nameS, descriptionS, category);
                WalletHolder walletHolder = new WalletHolder(walletCreate, searchUserAdapter.getSelectedUser());
                WalletService walletService = new WalletService(CreateWalletActivity.this);
                walletService.createWallet(accesToken, walletHolder);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}