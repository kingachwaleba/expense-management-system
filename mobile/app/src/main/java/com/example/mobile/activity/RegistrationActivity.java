package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.model.RegistrationForm;
import com.example.mobile.model.User;
import com.example.mobile.service.UserService;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText loginEt, emailEt, passwordEt, passwordConfirmEt;
    CheckBox statueCb;
    Button signUpBtn;
    TextView statueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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
        statueTv = findViewById(R.id.statue_reg_tv);

        signUpBtn.setOnClickListener(v -> {
            UserService userService = new UserService(RegistrationActivity.this);
            if (validateUser() != null)
                userService.register(new RegistrationForm(validateUser(), passwordConfirmEt.getText().toString()));
        });

        statueTv.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, StatueActivity.class);
            startActivity(intent);
        });
    }

    public User validateUser() {
        if (validateLogin(loginEt) && validateEmail(emailEt) && validatePassword(passwordEt) && validateConfirmPassword(passwordEt, passwordConfirmEt) && validateReg())
            return new User(loginEt.getText().toString(), emailEt.getText().toString(), passwordEt.getText().toString());
        else return null;
    }

    public boolean validateLogin(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawny format loginu. Min 5 znaków.");
            return false;
        }
    }

    public boolean validateEmail(EditText s) {
        if (Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawny format adresu email");
            return false;
        }
    }

    public boolean validatePassword(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,50}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawna forma hasła. Hasło ma składać się z min. 8 znaków w tym 1 cyfra, 1 duża litera, 1 mała litera");
            return false;
        }
    }

    public boolean validateConfirmPassword(EditText s1, EditText s2) {
        if ((s1.getText().toString()).equals(s2.getText().toString())) return true;
        else {
            s2.setError("Niepoprawne hasło");
            return false;
        }
    }

    public boolean validateReg() {
        if (statueCb.isChecked()) return true;
        else {
            Toast.makeText(RegistrationActivity.this, "Akceptacja regulaminu jest wymagana", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}