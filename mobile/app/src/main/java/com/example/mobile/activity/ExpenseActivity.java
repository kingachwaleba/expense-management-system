package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.ExpenseDetail;
import com.example.mobile.model.Member;
import com.example.mobile.model.User;
import com.example.mobile.service.ExpenseService;
import com.example.mobile.service.adapter.MemberAdapter;
import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends BaseActivity {

    String accessToken;
    int expenseId, walletId;
    ExpenseService expenseService;
    User expenseOwner;

    TextView nameExpenseTv, makeWhoTv, categoryTv, dateTv, costTv;
    Button editExpenseBtn, deleteExpenseBtn;
    RecyclerView forWhoRv;
    MemberAdapter memberAdapter;
    List<Member> seletedUsers, walletUsers;
    String nameExpense, costExpense, categoryExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        accessToken = getIntent().getStringExtra("accessToken");
        expenseId = getIntent().getIntExtra("expenseId", 0);
        walletId = getIntent().getIntExtra("walletId", 0);
        walletUsers = getIntent().getParcelableArrayListExtra("allMembers");
        Log.d("aaa",  " " + walletId);

        expenseService = new ExpenseService(this);

        nameExpenseTv = findViewById(R.id.name_tv);
        makeWhoTv = findViewById(R.id.owner_expense_tv);
        categoryTv = findViewById(R.id.category_expanse_tv);
        dateTv = findViewById(R.id.date_expense_tv);
        costTv = findViewById(R.id.cost_expense_tv);
        editExpenseBtn = findViewById(R.id.edit_expense_btn);
        deleteExpenseBtn = findViewById(R.id.delete_expense_btn);
        forWhoRv = findViewById(R.id.for_who_rv);

        seletedUsers = new ArrayList<>();
        memberAdapter = new MemberAdapter(this, seletedUsers, session.getUserDetails().get(SessionManager.KEY_LOGIN));
        forWhoRv.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
        forWhoRv.setAdapter(memberAdapter);


        expenseService.getExpenseById(expense -> {
            nameExpense = expense.getName();
            costExpense = String.valueOf(expense.getTotal_cost());
            categoryExpense = expense.getCategory().getName();
            expenseOwner = expense.getUser();
            String expenseOwner = getResources().getString(R.string.who_make_label) + " " +  expense.getUser().getLogin();
            String cost = getResources().getString(R.string.cost_label) + " " +  expense.getTotal_cost();
            String category = getResources().getString(R.string.category_label) + " " +  expense.getCategory().getName();
            String date = getResources().getString(R.string.date_label) + " " +  expense.getDate();
            nameExpenseTv.setText(expense.getName());
            makeWhoTv.setText(expenseOwner);
            costTv.setText(cost);
            categoryTv.setText(category);
            dateTv.setText(date);
            seletedUsers.clear();
            for(ExpenseDetail item : expense.getExpenseDetailsSet()) {
                item.getMember().setBalance(item.getCost());
                seletedUsers.add(item.getMember());
            }

            memberAdapter.notifyDataSetChanged();

        }, accessToken, expenseId);

        editExpenseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, EditExpenseActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("expenseId", expenseId);
            intent.putExtra("nameExpense", nameExpense);
            intent.putExtra("costExpense", costExpense);
            intent.putExtra("categoryExpense", categoryExpense);
            intent.putExtra("expenseOwner", expenseOwner);
            //intent.putExtra("periodExpanse", expense1.getPeriod());
            intent.putParcelableArrayListExtra("selectedUsers", (ArrayList<? extends Parcelable>) seletedUsers);
            intent.putParcelableArrayListExtra("walletUsers", (ArrayList<? extends Parcelable>) walletUsers);
            startActivity(intent);
        });

        deleteExpenseBtn.setOnClickListener(v -> {
            expenseService.deleteExpense(accessToken, expenseId);
            finish();
        });
    }

}