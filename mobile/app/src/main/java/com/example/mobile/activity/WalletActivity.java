package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.fragment.MembersFragment;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.User;
import com.example.mobile.service.ListService;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.ListsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    SessionManager session;

    int id;
    Boolean showMembersControl, showListsControl;
    String accessToken;
    String TAG = "MEMBERS_FRAGMENT";

    TextView walletNameTv, walletCategoryTv, descriptionTv, ownerTv, numberOfMembersTv;
    Button showMembersBtn, addMemberBtn, editWalletBtn, showListsBtn, addListBtn;
    int categoryId;
    String walletName, walletDescription, walletCategory;
    ListsAdapter listAdapter;
    RecyclerView shopListsRv;
    List<ListShop> lists1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new SessionManager(getApplicationContext());

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        showMembersControl = false;
        showListsControl=false;

        walletNameTv = findViewById(R.id.name_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ownerTv = findViewById(R.id.owner_tv);
        numberOfMembersTv = findViewById(R.id.number_of_members_tv);
        showMembersBtn = findViewById(R.id.show_members_btn);
        addMemberBtn = findViewById(R.id.add_member_btn);
        showMembersBtn.setBackgroundResource(R.drawable.btn_list_closed);
        editWalletBtn = findViewById(R.id.edit_wallet_btn);
        addListBtn = findViewById(R.id.add_shop_list_btn);
        showListsBtn = findViewById(R.id.show_shop_lists_btn);
        walletCategoryTv = findViewById(R.id.category_tv);

        shopListsRv = findViewById(R.id.shop_lists_rv);
        lists1 = new ArrayList<>();
        shopListsRv.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
        listAdapter = new ListsAdapter(this, lists1, accessToken);
        shopListsRv.setAdapter(listAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            walletName = walletModel.getName();
            walletCategory = walletModel.getCategory().getName();
            walletDescription = walletModel.getDescription();
            walletNameTv.setText(walletModel.getName());
            walletCategoryTv.setText(getResources().getString(R.string.category_label) + " " + walletModel.getCategory().getName());
            if(walletModel.getDescription()!=null)
                descriptionTv.setText(getResources().getString(R.string.description_label) + " " + walletModel.getDescription());
            ownerTv.setText(getResources().getString(R.string.owner_label) + " " + walletModel.getOwner());
            numberOfMembersTv.setText(getResources().getString(R.string.number_of_members_label) + " " + walletModel.getUserListCounter());
            showMembersBtn.setOnClickListener(v -> {
                if (!showMembersControl) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("members", (ArrayList<User>)walletModel.getUserList());
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, MembersFragment.class, bundle, TAG)
                                .commit();
                        showMembersBtn.setBackgroundResource(R.drawable.btn_list_opened);
                        showMembersControl = true;
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if(fragment != null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    showMembersBtn.setBackgroundResource(R.drawable.btn_list_closed);
                    showMembersControl = false;
                }
            });
        }, accessToken, id);


        addMemberBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, AddMemberActivity.class);
            intent.putExtra("name",walletNameTv.getText().toString());
            intent.putExtra("walletId",id);
            intent.putExtra("accessToken", accessToken);
            startActivity(intent);
        });

        editWalletBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, EditWalletActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletId",id);
            intent.putExtra("walletName", walletName);
            intent.putExtra("walletDescription", walletDescription);
            intent.putExtra("walletCategory", walletCategory);
            startActivity(intent);
        });

        addListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, CreateListActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletId",id);
            startActivity(intent);
        });


        showListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListService listService = new ListService(getParent());
                listService.getAllLists(lists -> {
                    if (!showListsControl) {
                        ListsAdapter listsAdapter1 = new ListsAdapter(getApplicationContext(), lists, accessToken);
                        shopListsRv.setAdapter(listsAdapter1);
                        listsAdapter1.notifyDataSetChanged();
                        showListsBtn.setBackgroundResource(R.drawable.btn_list_opened);
                        showListsControl = true;
                    } else {

                        showListsBtn.setBackgroundResource(R.drawable.btn_list_closed);
                        showListsControl = false;
                    }
                }, accessToken, id);
            }
        });
    }
}