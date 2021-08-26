package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.UpdatePasswordHolder;
import com.example.mobile.service.AccountService;

import java.util.Objects;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPasswordEt, newPasswordEt, newPasswordConfirmEt;
    Button saveChangesBtn;
    SessionManager session;
    String accessToken;
    AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        oldPasswordEt = findViewById(R.id.password_old_et);
        newPasswordEt = findViewById(R.id.password_new_et);
        newPasswordConfirmEt = findViewById(R.id.password_new_confirm_et);
        saveChangesBtn = findViewById(R.id.save_changes_new_password_btn);

        session = new SessionManager(this);
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        accountService = new AccountService(this);

        saveChangesBtn.setOnClickListener(v -> {
            if(validateDataPassword()!=null)
                accountService.changePassword(accessToken,validateDataPassword());
        });
    }



    public UpdatePasswordHolder validateDataPassword() {
        String op = oldPasswordEt.getText().toString();
        String np = newPasswordEt.getText().toString();
        String npc = newPasswordConfirmEt.getText().toString();
        if (validateOldPassword(op) && validateNewPassword(np) && validateConfirmNewPassword(np, npc))
            return new UpdatePasswordHolder(np, op);
        else return null;
    }


    public boolean validateOldPassword(String s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(ChangePasswordActivity.this, "Incorrect password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validateNewPassword(String s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(ChangePasswordActivity.this, "Incorrect password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validateConfirmNewPassword(String s1, String s2) {
        if (s1.equals(s2)) return true;
        else {
            Toast.makeText(ChangePasswordActivity.this, "Incorret confirm password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}