package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile.R;
import com.example.mobile.model.LoginForm;
import com.example.mobile.service.UserService;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    TextView signUpTv;
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

        signUpTv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(intent);
        });

        logInBtn.setOnClickListener(v -> {
           UserService userService = new UserService(LoginActivity.this);
           userService.login(validateLoginForm());
        });
    }

    public LoginForm validateLoginForm() {
        String e = emailEt.getText().toString();
        String p = passwordEt.getText().toString();
        if (validateEmail(e) && validatePassword(p))
            return new LoginForm(e,p);
        else return null;
    }

    public boolean validateEmail(String s) {
        if (Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(LoginActivity.this, "Incorrect format of an email (example@gmail.com)!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validatePassword(String s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
