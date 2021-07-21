package com.example.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends Fragment {
    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView goToRegistration = view.findViewById(R.id.sign_up_label);
        goToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegistrationActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });
        return view;
    }
}
