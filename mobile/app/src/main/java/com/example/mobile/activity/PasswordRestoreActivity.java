package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.mobile.R;
import com.example.mobile.service.UserService;
import java.util.Objects;
import java.util.regex.Pattern;

public class PasswordRestoreActivity extends AppCompatActivity {

    EditText emailEt;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_restore);

        emailEt = findViewById(R.id.email_reg_et);
        sendBtn = findViewById(R.id.restore_password_btn);

        sendBtn.setOnClickListener(v -> {
            if(validateEmail(emailEt)){
                UserService userService = new UserService(getApplicationContext());
                userService.restorePassword(emailEt.getText().toString());
            } else emailEt.setError("Podaj poprawny email");
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public boolean validateEmail(EditText s) {
        if (Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawna forma adresu email");
            return false;
        }
    }
}