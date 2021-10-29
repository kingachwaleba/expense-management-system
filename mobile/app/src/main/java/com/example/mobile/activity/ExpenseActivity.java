package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.model.ExpenseDetail;
import com.example.mobile.model.Member;
import com.example.mobile.model.User;
import com.example.mobile.service.ExpenseService;
import com.example.mobile.service.adapter.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends BaseActivity {

    TextView nameExpenseTv, makeWhoTv, categoryTv, dateTv, costTv;
    Button editExpenseBtn, deleteExpenseBtn;
    ImageView receiptIv;
    RecyclerView forWhoRv;

    UserListAdapter userListAdapter;
    List<Member> seletedUsers, walletUsers;
    String nameExpense, costExpense, categoryExpense, receiptPath, accessToken;
    int expenseId;
    ExpenseService expenseService;
    User expenseOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        accessToken = getIntent().getStringExtra("accessToken");
        expenseId = getIntent().getIntExtra("expenseId", 0);
        walletUsers = getIntent().getParcelableArrayListExtra("allMembers");

        expenseService = new ExpenseService(this);
        nameExpenseTv = findViewById(R.id.name_tv);
        makeWhoTv = findViewById(R.id.owner_expense_tv);
        categoryTv = findViewById(R.id.category_expanse_tv);
        dateTv = findViewById(R.id.date_expense_tv);
        costTv = findViewById(R.id.cost_expense_tv);
        editExpenseBtn = findViewById(R.id.edit_expense_btn);
        deleteExpenseBtn = findViewById(R.id.delete_expense_btn);
        forWhoRv = findViewById(R.id.for_who_rv);
        receiptIv = findViewById(R.id.receipt_iv);

        seletedUsers = new ArrayList<>();
        userListAdapter = new UserListAdapter(this, seletedUsers, "USER_EXPENSE");
        forWhoRv.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
        forWhoRv.setAdapter(userListAdapter);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        expenseService.getExpenseById(expense -> {
            nameExpense = expense.getExpense().getName();
            costExpense = String.valueOf(expense.getExpense().getTotal_cost());
            categoryExpense = expense.getExpense().getCategory().getName();
            expenseOwner = expense.getExpense().getUser();
            receiptPath = expense.getExpense().getReceipt_image();
            String expenseOwner = getResources().getString(R.string.who_make_label) + " " + expense.getExpense().getUser().getLogin();
            String cost = getResources().getString(R.string.cost_label) + " " + expense.getExpense().getTotal_cost();
            String category = getResources().getString(R.string.category_label) + " " + expense.getExpense().getCategory().getName();
            String date = getResources().getString(R.string.date_label) + " " + expense.getExpense().getDate();
            nameExpenseTv.setText(expense.getExpense().getName());
            makeWhoTv.setText(expenseOwner);
            costTv.setText(cost);
            categoryTv.setText(category);
            dateTv.setText(date.replace("T", " "));
            seletedUsers.clear();
            for (ExpenseDetail item : expense.getExpense().getExpenseDetailsSet()) {
                item.getMember().setBalance(item.getCost());
                seletedUsers.add(item.getMember());
            }

            if (expense.getExpense().getReceipt_image() != null) {
                ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into(receiptIv), getApplicationContext(), accessToken, expense.getExpense().getReceipt_image());
            }

            if (expense.getDeletedUserList() == null) {
                editExpenseBtn.setVisibility(View.GONE);
                deleteExpenseBtn.setVisibility(View.GONE);
            }

            userListAdapter.notifyDataSetChanged();

        }, accessToken, expenseId);

        editExpenseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, EditExpenseActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("expenseId", expenseId);
            intent.putExtra("nameExpense", nameExpense);
            intent.putExtra("costExpense", costExpense);
            intent.putExtra("categoryExpense", categoryExpense);
            intent.putExtra("receipt", receiptPath);
            intent.putParcelableArrayListExtra("selectedUsers", (ArrayList<? extends Parcelable>) seletedUsers);
            intent.putParcelableArrayListExtra("walletUsers", (ArrayList<? extends Parcelable>) walletUsers);
            startActivity(intent);
        });

        deleteExpenseBtn.setOnClickListener(v -> expenseService.deleteExpense(accessToken, expenseId));
    }
}