package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.service.adapter.UserListAdapter;

public class EditMembersActivity extends BaseActivity {

    Button goToSendInvitationBtn;
    RecyclerView membersRv;
    TextView walletNameTv;
    UserListAdapter userListAdapter;
    String accessToken, login;
    Boolean owner;
    WalletCreate walletCreate;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_members);

        session = new SessionManager(this);

        goToSendInvitationBtn = findViewById(R.id.send_invitations_btn);
        membersRv = findViewById(R.id.browse_members_add_rv);
        walletNameTv = findViewById(R.id.name_wallet_tv);

        walletNameTv.setText(getIntent().getStringExtra("name"));

        accessToken = getIntent().getStringExtra("accessToken");
        walletCreate = getIntent().getParcelableExtra("wallet");
        login = getIntent().getStringExtra("login");
        walletNameTv.setText(walletCreate.getName());

        owner = walletCreate.getOwner().equals(session.getUserDetails().get(SessionManager.KEY_LOGIN));

        userListAdapter = new UserListAdapter(this, walletCreate.getUserList(), accessToken, walletCreate.getId(), owner, login, "USER_EDIT");
        membersRv.setLayoutManager(new LinearLayoutManager(EditMembersActivity.this));
        membersRv.setAdapter(userListAdapter);

        goToSendInvitationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditMembersActivity.this, AddMemberActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("name", walletCreate.getName());
            intent.putExtra("walletId", walletCreate.getId());
            startActivity(intent);
            finish();
        });
    }
}