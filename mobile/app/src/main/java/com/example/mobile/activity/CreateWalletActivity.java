package com.example.mobile.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Category;
import com.example.mobile.model.Member;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.model.WalletHolder;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateWalletActivity extends BaseActivity {

    RadioGroup categoryRg;
    EditText nameEt, descriptionEt, infixEt;
    Button createBtn, cancelBtn;
    RecyclerView browseMembersRv;

    SessionManager session;
    WalletService walletService;
    WalletCreate walletCreate;
    Category category;
    String accessToken;
    List<Category> categories;
    List<Member> membersInit;
    UserListAdapter userListAdapterInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        walletService = new WalletService(this);

        session = new SessionManager(getApplicationContext());

        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        nameEt = findViewById(R.id.name_et);
        descriptionEt = findViewById(R.id.description_et);
        infixEt = findViewById(R.id.infix_et);
        createBtn = findViewById(R.id.create_wallet_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        categoryRg = findViewById(R.id.category_RG);

        browseMembersRv = findViewById(R.id.browse_members_rv);
        browseMembersRv.setLayoutManager(new LinearLayoutManager(this));
        membersInit = new ArrayList<>();
        userListAdapterInit = new UserListAdapter(this, membersInit, "USER_BROWSER");
        browseMembersRv.setAdapter(userListAdapterInit);

        infixEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (infixEt.getText().toString().length() > 0) {
                    walletService.getMembersByInfix(members -> {
                        if (members.size() > 0) {
                            UserListAdapter userListAdapter = new UserListAdapter(CreateWalletActivity.this, members, "USER_BROWSER");
                            browseMembersRv.setAdapter(userListAdapter);
                            userListAdapter.notifyDataSetChanged();
                        }
                    }, accessToken, infixEt.getText().toString());
                }

                if (infixEt.getText().toString().length() == 0) {
                    userListAdapterInit.clear();
                    browseMembersRv.setAdapter(userListAdapterInit);
                    userListAdapterInit.notifyDataSetChanged();
                }
            }
        });

        categories = MainActivity.getCategoriesWallet();
        for (int i = 0; i < categories.size(); i++) {
            RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
            rdbtn.setId(categories.get(i).getId());
            rdbtn.setText(categories.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            categoryRg.addView(rdbtn);
            if (i == 0) {
                rdbtn.setChecked(true);
                category = new Category(rdbtn.getId(), categories.get(i).getName());
            }
        }

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            String radioText = rb.getText().toString();
            category = new Category(checkedId, radioText);
        });

        createBtn.setOnClickListener(v -> {
            String nameS = nameEt.getText().toString();
            if (validateName(nameS)) {
                String descriptionS = descriptionEt.getText().toString();
                walletCreate = new WalletCreate(nameS, descriptionS, category);
                WalletHolder walletHolder = new WalletHolder(walletCreate, userListAdapterInit.getSelectedUser());
                walletService.createWallet(accessToken, walletHolder);
                userListAdapterInit.clearSelected();
            } else nameEt.setError("Podaj nazwe portfela!");
        });

        cancelBtn.setOnClickListener(v -> {
            userListAdapterInit.clearSelected();
            finish();
        });
    }

    public boolean validateName(String s) {
        return s.length() > 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userListAdapterInit.clearSelected();
        this.finish();
    }
}