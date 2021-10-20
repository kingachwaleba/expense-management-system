package com.example.mobile.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.Member;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.service.ExpenseService;
import com.example.mobile.service.ListService;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.ExpensesAdapter;
import com.example.mobile.service.adapter.ListsAdapter;
import com.example.mobile.service.adapter.MemberAdapter;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends BaseActivity {

    SessionManager session;

    int id;
    Boolean showMembersControl, showListsControl, showExpensesControl;
    String accessToken;
    TextView walletNameTv, walletCategoryTv, descriptionTv, ownerTv, numberOfMembersTv, walletExpensesTv, userExpensesTv, userBalanceTv;
    Button  addMemberBtn, editWalletBtn, addListBtn, goToChatBtn, addExpenseBtn, deleteWalletBtn, leaveWalletBtn, statisticsBtn;
    String walletName, walletDescription, walletCategory;
    ListsAdapter listAdapter;
    ExpensesAdapter expensesAdapter;
    RecyclerView shopListsRv, expensesRv, membersRv;
    List<ListShop> lists1;
    List<Expense> expenses1;
    List<Member> members1, members;
    MemberAdapter memberAdapter;
    ExpenseService expenseService;
    ImageButton showMembersBtn, showListsBtn, showExpensesBtn;
    LinearLayout membersListL;
    TextView editMembersBtn;

    WalletCreate walletCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        session = new SessionManager(getApplicationContext());

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        showMembersControl = false;
        showListsControl = false;
        showExpensesControl = false;
        expenseService = new ExpenseService(this);

        walletNameTv = findViewById(R.id.name_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ownerTv = findViewById(R.id.owner_tv);
        numberOfMembersTv = findViewById(R.id.number_of_members_tv);
        showMembersBtn = findViewById(R.id.show_members_btn);
        addMemberBtn = findViewById(R.id.add_member_btn);
        editWalletBtn = findViewById(R.id.edit_wallet_btn);
        addListBtn = findViewById(R.id.add_shop_list_btn);
        showListsBtn = findViewById(R.id.show_shop_lists_btn);
        walletCategoryTv = findViewById(R.id.category_tv);
        goToChatBtn = findViewById(R.id.open_chat_btn);
        addExpenseBtn = findViewById(R.id.add_expense_btn);
        showExpensesBtn = findViewById(R.id.show_expenses_btn);
        deleteWalletBtn = findViewById(R.id.delete_wallet_btn);
        leaveWalletBtn = findViewById(R.id.leave_wallet_btn);

        walletExpensesTv = findViewById(R.id.wallet_expenses_tv);
        userExpensesTv = findViewById(R.id.your_expanses_tv);
        userBalanceTv = findViewById(R.id.your_balance_tv);
        statisticsBtn = findViewById(R.id.statistics_btn);

        membersListL = findViewById(R.id.members_list_l);
        editMembersBtn = findViewById(R.id.edit_members_btn);

        membersRv = findViewById(R.id.members_wallet_rv);
        members1 = new ArrayList<>();
        membersRv.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
        memberAdapter = new MemberAdapter(this, members1, session.getUserDetails().get(SessionManager.KEY_LOGIN), accessToken, id);
        membersRv.setAdapter(memberAdapter);


        shopListsRv = findViewById(R.id.shop_lists_rv);
        lists1 = new ArrayList<>();
        shopListsRv.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
        listAdapter = new ListsAdapter(this, lists1, accessToken);
        shopListsRv.setAdapter(listAdapter);

        expensesRv = findViewById(R.id.expenses_rv);
        expenses1 = new ArrayList<>();
        expensesRv.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
        expensesAdapter = new ExpensesAdapter(this, expenses1, accessToken, members);
        expensesRv.setAdapter(expensesAdapter);

        goToChatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, ChatActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletId", id);
            startActivity(intent);
        });

        statisticsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, StatisticsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletName", walletName);
            intent.putExtra("walletId", id);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            walletCreate = walletModel;
            walletName = walletModel.getName();
            walletCategory = walletModel.getCategory().getName();
            walletDescription = walletModel.getDescription();
            members = walletModel.getUserList();
            String categoryText = getResources().getString(R.string.category_label) + " " + walletCategory;
            String descriptionText;
            String ownerText = getResources().getString(R.string.owner_label) + " " + walletModel.getOwner();
            String numberOfMembersText = getResources().getString(R.string.number_of_members_label) + " " + walletModel.getUserListCounter();

            String walletExpensesS = getResources().getString(R.string.all_balance_label) + " " + walletModel.getWalletExpensesCost();
            String userExpensesS = getResources().getString(R.string.your_expanses_label) + " " + walletModel.getUserExpensesCost();
            String yourBalanceS = getResources().getString(R.string.your_balance_label) + " " + walletModel.getLoggedInUserBalance();

            walletNameTv.setText(walletModel.getName());
            walletCategoryTv.setText(categoryText);

            walletExpensesTv.setText(walletExpensesS);
            userExpensesTv.setText(userExpensesS);
            userBalanceTv.setText(yourBalanceS);

            if(walletDescription!=null){
                descriptionText = getResources().getString(R.string.description_label) + " " + walletDescription;
                descriptionTv.setText(descriptionText);
            }

            if(!walletCreate.getOwner().equals(session.getUserDetails().get(SessionManager.KEY_LOGIN))) deleteWalletBtn.setVisibility(View.GONE);


            ownerTv.setText(ownerText);
            numberOfMembersTv.setText(numberOfMembersText);
            showMembersBtn.setOnClickListener(v -> {
                if (!showMembersControl) {
                    membersListL .setVisibility(View.VISIBLE);
                    MemberAdapter memberAdapter1 = new MemberAdapter(getApplicationContext(), members, session.getUserDetails().get(SessionManager.KEY_LOGIN), accessToken, id);
                    membersRv.setAdapter(memberAdapter1);
                    memberAdapter1.notifyDataSetChanged();
                    showMembersBtn.setBackgroundResource(R.drawable.btn_list_opened);
                    showMembersControl = true;
                } else {
                    membersListL.setVisibility(View.GONE);
                    membersRv.setAdapter(memberAdapter);
                    memberAdapter.notifyDataSetChanged();
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
            intent.putParcelableArrayListExtra("members", (ArrayList<Member>)members);
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


        showListsBtn.setOnClickListener(v -> {
            ListService listService = new ListService(getParent());
            listService.getAllLists(lists -> {
                if (!showListsControl) {
                    ListsAdapter listsAdapter1 = new ListsAdapter(getApplicationContext(), lists, accessToken);
                    shopListsRv.setAdapter(listsAdapter1);
                    listsAdapter1.notifyDataSetChanged();
                    showListsBtn.setBackgroundResource(R.drawable.btn_list_opened);
                    showListsControl = true;
                } else {
                    shopListsRv.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    showListsBtn.setBackgroundResource(R.drawable.btn_list_closed);
                    showListsControl = false;
                }
            }, accessToken, id);
        });

        showExpensesBtn.setOnClickListener(v -> expenseService.getAllExpenses(expenses -> {
            if (!showExpensesControl) {
                ExpensesAdapter expensesAdapter1 = new ExpensesAdapter(getApplicationContext(), expenses, accessToken, members);
                expensesRv.setAdapter(expensesAdapter1);
                expensesAdapter1.notifyDataSetChanged();
                showExpensesBtn.setBackgroundResource(R.drawable.btn_list_opened);
                showExpensesControl = true;
            } else {
                expensesRv.setAdapter(expensesAdapter);
                expensesAdapter.notifyDataSetChanged();
                showExpensesBtn.setBackgroundResource(R.drawable.btn_list_closed);
                showExpensesControl = false;
            }
        }, accessToken, id));

        addExpenseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, CreateExpenseActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletId", id);
            intent.putParcelableArrayListExtra("members", (ArrayList<? extends Parcelable>) walletCreate.getUserList());
            startActivity(intent);
        });

        editMembersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, EditMembersActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("wallet", walletCreate);
            startActivity(intent);
        });

        deleteWalletBtn.setOnClickListener(v -> new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Usuwanie portfela")
                .setMessage("Czy na pewno chcesz usunać portfel?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    walletService.deleteWallet(accessToken, walletCreate.getId());
                    finish();
                })
                .setNegativeButton(android.R.string.no, null).show());

        leaveWalletBtn.setOnClickListener(v -> new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("Opuszczanie portfela")
                .setMessage("Czy na pewno chcesz opuścić ten portfel?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    walletService.deleteCurrentMember(accessToken, walletCreate.getId());
                    finish();
                }).setNegativeButton(android.R.string.no, null).show());
    }
}

