package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.model.LoginForm;
import com.example.mobile.service.UserService;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    TextView signUpTv, forgotPasswordTv;
    Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        signUpTv = findViewById(R.id.sign_up_label);
        logInBtn = findViewById(R.id.log_in_btn);
        emailEt = findViewById(R.id.email_reg_et);
        passwordEt = findViewById(R.id.password_reg_et);
        forgotPasswordTv = findViewById(R.id.password_forget_label);

        forgotPasswordTv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PasswordRestoreActivity.class);
            startActivity(intent);
        });

        signUpTv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        logInBtn.setOnClickListener(v -> {
            UserService userService = new UserService(LoginActivity.this);
            if (validateLoginForm() != null) {
                userService.login(validateLoginForm());
            }
        });
    }

    public LoginForm validateLoginForm() {
        if (validateEmail(emailEt) && validatePassword(passwordEt))
            return new LoginForm(emailEt.getText().toString(), passwordEt.getText().toString());
        else return null;
    }

    public boolean validateEmail(EditText s) {
        if (Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawna forma adresu email");
            return false;
        }
    }

    public boolean validatePassword(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,50}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawne hasło");
            return false;
        }
    }

}
