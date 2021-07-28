package com.example.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.model.LoginForm;
import com.example.mobile.service.UserService;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView goToRegistration = findViewById(R.id.sign_up_label);
        Button log_btn = findViewById(R.id.log_btn);
        EditText email= findViewById(R.id.email_edit);
        EditText password = findViewById(R.id.password_edit);

        goToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserService userService = new UserService(LoginActivity.this);
               userService.login(new LoginForm(email.getText().toString(), password.getText().toString()));
            }
        });
    }
}
