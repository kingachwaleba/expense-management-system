package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mobile.R;
import com.example.mobile.model.User;
import com.example.mobile.service.UserService;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText loginEt, emailEt, passwordEt, passwordConfirmEt;
    CheckBox statueCb;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        signUpBtn = findViewById(R.id.sign_up_reg_btn);
        loginEt = findViewById(R.id.login_reg_et);
        emailEt = findViewById(R.id.email_reg_et);
        passwordEt = findViewById(R.id.password_reg_et);
        passwordConfirmEt = findViewById(R.id.password_confirm_reg_et);
        statueCb = findViewById(R.id.statue_reg_cb);

        signUpBtn.setOnClickListener(v -> {
            UserService userService = new UserService(RegistrationActivity.this);
            if (validateUser() != null)
                userService.register(validateUser());
        });
    }

    public User validateUser() {
        String l = loginEt.getText().toString();
        String e = emailEt.getText().toString();
        String p = passwordEt.getText().toString();
        String pc = passwordConfirmEt.getText().toString();
        if (validateLogin(l) && validateEmail(e) && validatePassword(p) && validateConfirmPassword(p, pc) && validateReg())
            return new User(l, e, p);
        else return null;
    }

    public boolean validateLogin(String s) {
        if (Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Incorrect format of a login (it could contain letters, numbers, -, " + "it should start with letter, it should have 5-45 characters)!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validateEmail(String s) {
        if (Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Incorrect format of an email (example@gmail.com)!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validatePassword(String s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s).matches())
            return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Incorrect password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validateConfirmPassword(String s1, String s2) {
        if (s1.equals(s2)) return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Incorret confirm password!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validateReg() {
        if (statueCb.isChecked()) return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Consend to the regulations is required", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}