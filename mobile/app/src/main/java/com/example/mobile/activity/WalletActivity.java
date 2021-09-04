package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.fragment.MembersFragment;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.User;
import com.example.mobile.service.ExpenseService;
import com.example.mobile.service.ListService;
import com.example.mobile.service.WalletService;
import com.example.mobile.service.adapter.ExpensesAdapter;
import com.example.mobile.service.adapter.ListsAdapter;
import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends BaseActivity {

    SessionManager session;

    int id;
    Boolean showMembersControl, showListsControl, showExpensesControl;
    String accessToken;
    String TAG = "MEMBERS_FRAGMENT";

    TextView walletNameTv, walletCategoryTv, descriptionTv, ownerTv, numberOfMembersTv;
    Button showMembersBtn, addMemberBtn, editWalletBtn, showListsBtn, addListBtn, goToChatBtn, addExpenseBtn, showExpensesBtn;
    String walletName, walletDescription, walletCategory;
    ListsAdapter listAdapter;
    ExpensesAdapter expensesAdapter;
    RecyclerView shopListsRv, expensesRv;
    List<ListShop> lists1;
    List<Expense> expenses1;
    List<User> members;
    ExpenseService expenseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        session = new SessionManager(getApplicationContext());

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        showMembersControl = false;
        showListsControl=false;
        showExpensesControl=false;
        expenseService = new ExpenseService(this);

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
        goToChatBtn = findViewById(R.id.open_chat_btn);
        addExpenseBtn = findViewById(R.id.add_expense_btn);
        showExpensesBtn = findViewById(R.id.show_expenses_btn);

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

        addExpenseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, CreateExpenseActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("walletId", id);
            intent.putParcelableArrayListExtra("members", (ArrayList<User>)members);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        WalletService walletService = new WalletService(this);
        walletService.getWalletById(walletModel -> {
            members = walletModel.getUserList();
            walletName = walletModel.getName();
            walletCategory = walletModel.getCategory().getName();
            walletDescription = walletModel.getDescription();
            members = walletModel.getUserList();
            String categoryText = getResources().getString(R.string.category_label) + " " + walletCategory;
            String descriptionText;
            String ownerText = getResources().getString(R.string.owner_label) + " " + walletModel.getOwner();
            String numberOfMembersText = getResources().getString(R.string.number_of_members_label) + " " + walletModel.getUserListCounter();

            walletNameTv.setText(walletModel.getName());
            walletCategoryTv.setText(categoryText);

            if(walletDescription!=null){
                descriptionText = getResources().getString(R.string.description_label) + " " + walletDescription;
                descriptionTv.setText(descriptionText);
            }


            ownerTv.setText(ownerText);
            numberOfMembersTv.setText(numberOfMembersText);
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

                    showListsBtn.setBackgroundResource(R.drawable.btn_list_closed);
                    showListsControl = false;
                }
            }, accessToken, id);
        });

        showExpensesBtn.setOnClickListener(v -> expenseService.getAllExpenses(expenses -> {
            if (!showExpensesControl) {
                ExpensesAdapter expensesAdapter1 = new ExpensesAdapter(getApplicationContext(), expenses, accessToken, members);
                shopListsRv.setAdapter(expensesAdapter1);
                expensesAdapter1.notifyDataSetChanged();
                showExpensesBtn.setBackgroundResource(R.drawable.btn_list_opened);
                showExpensesControl = true;
            } else {

                showExpensesBtn.setBackgroundResource(R.drawable.btn_list_closed);
                showExpensesControl = false;
            }
        }, accessToken, id));


    }
}