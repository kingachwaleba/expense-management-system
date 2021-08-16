package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Member;
import com.example.mobile.service.WalletService;
import com.example.mobile.model.WalletModel;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    int id;
    SessionManager session;
    String accesToken;
    TextView walletName_tv, description_tv, owner_tv, number_of_members_tv;
    Button show_members;
    Boolean show_members_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        session = new SessionManager(getApplicationContext());
        accesToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        walletName_tv = findViewById(R.id.walletName_tv);
        description_tv = findViewById(R.id.description_tv);
        owner_tv = findViewById(R.id.owner_tv);
        number_of_members_tv = findViewById(R.id.number_of_members_tv);
        show_members = findViewById(R.id.show_members);
        show_members_control = false;
        show_members.setBackgroundResource(R.drawable.button_list_close);



    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            walletName_tv.setText(walletModel.getName());
            if(walletModel.getDescription()!=null)
                description_tv.setText(getResources().getString(R.string.description) + " " + walletModel.getDescription());
            owner_tv.setText(getResources().getString(R.string.owner) + " " + walletModel.getOwner());
            number_of_members_tv.setText(getResources().getString(R.string.number_of_members) + " " + String.valueOf(walletModel.getUserListCounter()));

            for(int i = 0; i < walletModel.getUserListCounter();i++){
                System.out.println(walletModel.getUserList().get(i).getLogin());
            }

          /*  Fragment fragment = getSupportFragmentManager().findFragmentByTag("MembersFragment");
            if(fragment != null){
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("members", (ArrayList<Member>)walletModel.getUserList());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, MembersFragment.class, bundle, "MembersFragment");
            }*/

            show_members.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show_members_control == false) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("members", (ArrayList<Member>)walletModel.getUserList());
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container_view, MembersFragment.class, bundle, "MembersFragment")
                                    .commit();
                            show_members.setBackgroundResource(R.drawable.button_list_active);
                            show_members_control = true;
                    } else {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("MembersFragment");
                        if(fragment != null)
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        show_members.setBackgroundResource(R.drawable.button_list_close);
                        show_members_control = false;
                    }
                }
            });



        }, accesToken, id);
    }

}