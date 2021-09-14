package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.ExpenseDetail;
import com.example.mobile.model.Member;
import com.example.mobile.service.ExpenseService;
import com.example.mobile.service.adapter.MemberAdapter;
import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends BaseActivity {

    String accessToken;
    int expenseId;
    ExpenseService expenseService;

    TextView nameExpenseTv, makeWhoTv, categoryTv, periodTv, dateTv, costTv;
    Button editExpenseBtn;
    RecyclerView forWhoRv;
    MemberAdapter memberAdapter;
    List<Member> members, allMembers;
    String nameExpense, costExpense, categoryExpense, periodExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        accessToken = getIntent().getStringExtra("accessToken");
        expenseId = getIntent().getIntExtra("expenseId", 0);
        allMembers = getIntent().getParcelableArrayListExtra("allMembers");

        expenseService = new ExpenseService(this);

        nameExpenseTv = findViewById(R.id.name_tv);
        makeWhoTv = findViewById(R.id.owner_expense_tv);
        categoryTv = findViewById(R.id.category_expanse_tv);
        periodTv = findViewById(R.id.period_expanse_tv);
        dateTv = findViewById(R.id.date_expense_tv);
        costTv = findViewById(R.id.cost_expense_tv);
        editExpenseBtn = findViewById(R.id.edit_expense_btn);
        forWhoRv = findViewById(R.id.for_who_rv);

        members = new ArrayList<>();
        memberAdapter = new MemberAdapter(this, members, session.getUserDetails().get(SessionManager.KEY_LOGIN));
        forWhoRv.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
        forWhoRv.setAdapter(memberAdapter);


        expenseService.getExpenseById(expense -> {
            nameExpense = expense.getName();
            costExpense = String.valueOf(expense.getTotal_cost());
            categoryExpense = expense.getCategory().getName();
           // periodExpense = expense.getPeriod();
            String expenseOwner = getResources().getString(R.string.who_make_label) + " " +  expense.getUser().getLogin();
           // String period = getResources().getString(R.string.period_label) + " " +  expense.getPeriod();
            String cost = getResources().getString(R.string.cost_label) + " " +  expense.getTotal_cost();
            String category = getResources().getString(R.string.category_label) + " " +  expense.getCategory().getName();
            String date = getResources().getString(R.string.date_label) + " " +  expense.getDate();
            nameExpenseTv.setText(expense.getName());
            makeWhoTv.setText(expenseOwner);
            costTv.setText(cost);
            categoryTv.setText(category);
            dateTv.setText(date);
            members.clear();
            for(ExpenseDetail item : expense.getExpenseDetailsSet()) {
                members.add(item.getMember());
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
            //intent.putExtra("periodExpanse", expense1.getPeriod());
            intent.putParcelableArrayListExtra("membersExpense", (ArrayList<? extends Parcelable>) members);
            intent.putParcelableArrayListExtra("allMembers", (ArrayList<? extends Parcelable>) allMembers);
            startActivity(intent);
        });

    }
}