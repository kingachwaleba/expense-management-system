package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;

public class EditMembersActivity extends BaseActivity {

    Button goToSendInvitation;
    RecyclerView memnersRv;
    TextView walletNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_members);

        goToSendInvitation = findViewById(R.id.send_invitations_btn);
        memnersRv = findViewById(R.id.browse_members_add_rv);
        walletNameTv = findViewById(R.id.name_wallet_tv);

    }
}