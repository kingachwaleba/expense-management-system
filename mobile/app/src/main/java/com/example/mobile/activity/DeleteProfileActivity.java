package com.example.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile.R;
import com.example.mobile.service.AccountService;

import java.util.regex.Pattern;

public class DeleteProfileActivity extends BaseActivity {

    Button deleteAccountBtn, cancelBtn;
    EditText passwordEt;
    AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        deleteAccountBtn = findViewById(R.id.delete_account_btn);
        cancelBtn = findViewById(R.id.cancel_account_delete_btn);
        passwordEt = findViewById(R.id.password_et);

        cancelBtn.setOnClickListener(v -> finish());
        accountService = new AccountService(this);

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword(passwordEt)) passwordEt.setError("Nie poprawne hasło");
                    else accountService.deleteAccount(passwordEt.getText().toString());
            }
        });
    }

    public boolean validatePassword(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawne hasło");
            return false;
        }
    }
}