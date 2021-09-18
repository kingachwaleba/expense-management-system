package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.model.Category;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import com.example.mobile.model.Member;
import com.example.mobile.model.User;
import com.example.mobile.service.ExpenseService;

import java.util.ArrayList;
import java.util.List;

public class EditExpenseActivity extends BaseActivity {

    String accessToken;
    int expenseId;
    ExpenseService expenseService;
    String nameExpense, costExpense, category;
    //String period
    List<Member> selectedUser, allMembers;
    List<String> selectedUsersLogin;
    List<Category> categoriesExpense;
    List<String> periods;
    RadioGroup categoryRg, periodsRg;
    LinearLayout membersCb;
    Button editExpenseBtn, cancelEditBtn;
    EditText nameExpenseEt, costExpenseEt;
    Category selectedCategory;
    String selectedPeriod;
    User expenseOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        accessToken = getIntent().getStringExtra("accessToken");
        expenseId = getIntent().getIntExtra("expenseId", 0);
        nameExpense = getIntent().getStringExtra("nameExpense");
        costExpense = getIntent().getStringExtra("costExpense");
        category = getIntent().getStringExtra("categoryExpense");
       // periodExpense = getIntent().getStringExtra("periodExpanse");
        selectedUser = getIntent().getParcelableArrayListExtra("selectedUsers");
        allMembers = getIntent().getParcelableArrayListExtra("walletUsers");
        expenseOwner = getIntent().getParcelableExtra("expenseOwner");

        nameExpenseEt = findViewById(R.id.name_expense_et);
        costExpenseEt = findViewById(R.id.cost_expense_et);
        categoryRg = findViewById(R.id.category_RG);
        periodsRg = findViewById(R.id.period_RG);
        membersCb = findViewById(R.id.members_expense_l);

        editExpenseBtn = findViewById(R.id.edit_expense_btn);
        cancelEditBtn = findViewById(R.id.cancel_edit_expense_btn);

        expenseService = new ExpenseService(this);

        nameExpenseEt.setText(nameExpense);
        costExpenseEt.setText(costExpense);

        categoriesExpense = MainActivity.getCategoriesExpense();
        periods = MainActivity.getPeriods();
        selectedUsersLogin = new ArrayList<>();

        for (int i = 0; i < selectedUser.size(); i++)
            selectedUsersLogin.add(selectedUser.get(i).getLogin());

        for(int i = 0; i < categoriesExpense.size(); i++){
            RadioButton rdbtn = new RadioButton(EditExpenseActivity.this);
            rdbtn.setId(categoriesExpense.get(i).getId());
            rdbtn.setText(categoriesExpense.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            categoryRg.addView(rdbtn);
            if(categoriesExpense.get(i).getName().equals(category)) rdbtn.setChecked(true);
        }

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            selectedCategory = new Category(checkedId, radioText);
        });

        for(int i = 0; i < periods.size(); i++){
            RadioButton rdbtn = new RadioButton(EditExpenseActivity.this);
            rdbtn.setId(i);
            rdbtn.setText(periods.get(i));
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            periodsRg.addView(rdbtn);
            if(i == 0) rdbtn.setChecked(true);
            //if(periods.get(i).equals(period)) rdbtn.setChecked(true);
        }

        periodsRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            selectedPeriod = rb.getText().toString();
        });

        for(int i = 0; i < allMembers.size(); i++){
            CheckBox cb = new CheckBox(EditExpenseActivity.this);
            cb.setId(allMembers.get(i).getUserId());
            cb.setText(allMembers.get(i).getLogin());
            cb.setTextAppearance(R.style.simple_label);
            cb.setTextSize(18);
            cb.setButtonDrawable(R.drawable.cb_simple);
            membersCb.addView(cb);
            if(selectedUsersLogin.contains(allMembers.get(i).getLogin())) cb.setChecked(true);
            cb.setOnClickListener(v -> {
                if(cb.isChecked()) selectedUsersLogin.add(cb.getText().toString());
                else selectedUsersLogin.remove(cb.getText().toString());
            });
        }

        cancelEditBtn.setOnClickListener(v -> finish());

        editExpenseBtn.setOnClickListener(v -> {
            if(nameExpenseEt.getText().toString().length()==0) nameExpenseEt.setError("Wpisz nazwe wydatku!");
            else if(costExpenseEt.getText().toString().length()==0) costExpenseEt.setError("Wpisz kwote wydatku!");
            else if(selectedUsersLogin.size()==0) Toast.makeText(EditExpenseActivity.this, "Wybierz osoby dla których zrobiony jest wydatek", Toast.LENGTH_LONG).show();
            else {
                Expense editExpense = new Expense(nameExpenseEt.getText().toString(), null, Double.parseDouble(costExpenseEt.getText().toString()), null, selectedCategory, expenseOwner);
                ExpenseHolder editExpenseHolder = new ExpenseHolder(editExpense, selectedUsersLogin);
                expenseService.editExpenseById(accessToken, expenseId, editExpenseHolder);
                finish();
            }
        });
    }
}