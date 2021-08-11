package com.example.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile.model.LoginForm;
import com.example.mobile.service.UserService;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TextView goToRegistration = findViewById(R.id.sign_up_label);
        Button log_btn = findViewById(R.id.log_btn);
        email= findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit);

        goToRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
            startActivity(intent);
        });

        log_btn.setOnClickListener(v -> {
           UserService userService = new UserService(LoginActivity.this);
           userService.login(validateLoginForm());
        });
    }

    public LoginForm validateLoginForm() {
        String e = email.getText().toString();
        String p = password.getText().toString();
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
