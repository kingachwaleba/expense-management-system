package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.mobile.R;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.SearchUserAdapter;
import java.util.ArrayList;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity {

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
        List<String> membersInit = new ArrayList<>();
        SearchUserAdapter searchUserAdapterInit = new SearchUserAdapter(this, membersInit);
        browseMembersRv.setAdapter(searchUserAdapterInit);

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
                            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(AddMemberActivity.this, members);
                            browseMembersRv.setAdapter(searchUserAdapter);
                            searchUserAdapter.notifyDataSetChanged();
                    }, accessToken, walletId, infixEt.getText().toString());
                }

                if(infixEt.getText().toString().length()==0){
                    searchUserAdapterInit.clear();
                    browseMembersRv.setAdapter(searchUserAdapterInit);
                    searchUserAdapterInit.notifyDataSetChanged();
                }

            }
        });


        sendInvitationsBtn.setOnClickListener(v -> {
            for(int i = 0; i < searchUserAdapterInit.getSelectedUser().size(); i++){
                walletService.sendInvitationToUser(accessToken, walletId, searchUserAdapterInit.getSelectedUser().get(i));
            }
            finish();
        });
    }
}