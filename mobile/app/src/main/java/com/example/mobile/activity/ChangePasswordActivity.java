package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile.R;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPasswordEt, newPasswordEt, newPasswordConfirmEt;
    Button saveChangesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordEt = findViewById(R.id.password_old_et);
        newPasswordEt = findViewById(R.id.password_new_et);
        newPasswordConfirmEt = findViewById(R.id.password_new_confirm_et);
        saveChangesBtn = findViewById(R.id.save_changes_new_password_btn);
    }
}