package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.UpdatePasswordHolder;
import com.example.mobile.service.AccountService;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends BaseActivity {

    EditText oldPasswordEt, newPasswordEt, newPasswordConfirmEt;
    Button saveChangesBtn;

    SessionManager session;
    AccountService accountService;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordEt = findViewById(R.id.password_old_et);
        newPasswordEt = findViewById(R.id.password_new_et);
        newPasswordConfirmEt = findViewById(R.id.password_new_confirm_et);
        saveChangesBtn = findViewById(R.id.save_changes_new_password_btn);

        session = new SessionManager(this);
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);

        accountService = new AccountService(this);

        saveChangesBtn.setOnClickListener(v -> {
            if (validateDataPassword() != null)
                accountService.changePassword(accessToken, validateDataPassword());
        });
    }

    public UpdatePasswordHolder validateDataPassword() {
        if (validateOldPassword(oldPasswordEt) && validateNewPassword(newPasswordEt) && validateConfirmNewPassword(newPasswordEt, newPasswordConfirmEt))
            return new UpdatePasswordHolder(newPasswordEt.getText().toString(), oldPasswordEt.getText().toString(), newPasswordConfirmEt.getText().toString());
        else return null;
    }

    public boolean validateOldPassword(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawne hasło");
            return false;
        }
    }

    public boolean validateNewPassword(EditText s) {
        if (Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(s.getText().toString()).matches())
            return true;
        else {
            s.setError("Niepoprawne hasło");
            return false;
        }
    }

    public boolean validateConfirmNewPassword(EditText s1, EditText s2) {
        if ((s1.getText().toString()).equals(s2.getText().toString())) return true;
        else {
            s2.setError("Niepoprawne hasło");
            return false;
        }
    }

}