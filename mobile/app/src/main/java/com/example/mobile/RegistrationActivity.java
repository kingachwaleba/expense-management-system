package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobile.config.RetrofitClient;
import com.example.mobile.model.User;
import com.example.mobile.service.UserService;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {

    EditText login, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        Button sign_up = findViewById(R.id.register_button);
        login =findViewById(R.id.login_edit);
        email = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    userSignUp();
            }
        });
    }

    public void userSignUp(){
        User user = new User(login.getText().toString(),email.getText().toString(),password.getText().toString());
        Call<Void> call = RetrofitClient.getInstance().getUserService().register(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(RegistrationActivity.this,"udalo sie",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}