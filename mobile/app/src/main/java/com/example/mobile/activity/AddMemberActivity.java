package com.example.mobile.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.UserListAdapter;
import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends BaseActivity {

    RecyclerView browseMembersRv;
    EditText infixEt;
    TextView nameTv;
    WalletService walletService;
    Button sendInvitationsBtn;
    int walletId;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        walletService = new WalletService(getParent());

        infixEt = findViewById(R.id.infix_add_et);
        nameTv = findViewById(R.id.name_tv);

        nameTv.setText(getIntent().getStringExtra("name"));
        walletId = getIntent().getIntExtra("walletId", 0);
        accessToken = getIntent().getStringExtra("accessToken");

        sendInvitationsBtn = findViewById(R.id.send_invitations_btn);

        browseMembersRv = findViewById(R.id.browse_members_add_rv);
        browseMembersRv.setLayoutManager(new LinearLayoutManager(this));
        List<Member> membersInit = new ArrayList<>();
        UserListAdapter userListAdapterInit = new UserListAdapter(this, membersInit, "USER_BROWSER");
        browseMembersRv.setAdapter(userListAdapterInit);

        infixEt.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(infixEt.getText().toString().length()>0){
                    walletService.getMembersByInfixInWallet(members -> {
                        if(members.size() > 0){
                            UserListAdapter userListAdapter =
                                    new UserListAdapter(AddMemberActivity.this, members,
                                            "USER_BROWSER");
                            browseMembersRv.setAdapter(userListAdapter);
                            userListAdapter.notifyDataSetChanged();}
                    }, accessToken, walletId, infixEt.getText().toString());
                }
                if(infixEt.getText().toString().length()==0){
                    userListAdapterInit.clear();
                    browseMembersRv.setAdapter(userListAdapterInit);
                    userListAdapterInit.notifyDataSetChanged();
                }
            }
        });

        sendInvitationsBtn.setOnClickListener(v -> {
            for(int i = 0; i < userListAdapterInit.getSelectedUser().size(); i++)
                walletService.sendInvitationToUser(accessToken, walletId, userListAdapterInit.getSelectedUser().get(i));
            userListAdapterInit.clearSelected();
            finish();
        });
    }
}