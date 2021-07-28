package com.example.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mobile.model.LoginForm;
import com.example.mobile.service.UserService;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        super(R.layout.fragment_login);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView goToRegistration = view.findViewById(R.id.sign_up_label);
        Button log_btn = view.findViewById(R.id.log_btn);
        EditText login = view.findViewById(R.id.email_edit);
        EditText password = view.findViewById(R.id.password_edit);

        goToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegistrationActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserService userService = new UserService(getActivity());
               userService.login(new LoginForm(login.getText().toString(), password.getText().toString()));
            }
        });

        return view;
    }
}
