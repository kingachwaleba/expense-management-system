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
import com.example.mobile.service.ExpenseService;
import java.util.ArrayList;
import java.util.List;

public class CreateExpenseActivity extends BaseActivity {

    EditText expenseNameEt, expenseCostEt;
    RadioGroup categoryRg;
    Button createExpenseBtn, cancelBtn;
    int walletId;
    String accessToken;
    List<Member> members;
    LinearLayout membersCb;
    List<Category> categoriesExpense;
    Category selectedCategory;
    List<String> selectedMebers;
    ExpenseService expenseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        walletId = getIntent().getIntExtra("walletId", 0);
        accessToken = getIntent().getStringExtra("accessToken");
        members = getIntent().getParcelableArrayListExtra("members");

        expenseService = new ExpenseService(this);

        expenseNameEt = findViewById(R.id.name_expense_et);
        expenseCostEt = findViewById(R.id.cost_et);
        categoryRg = findViewById(R.id.category_expense_RG);
        createExpenseBtn = findViewById(R.id.create_expense_btn);
        cancelBtn = findViewById(R.id.cancel_expense_btn);
        membersCb = findViewById(R.id.members_cb);

        cancelBtn.setOnClickListener(v -> finish());

        categoriesExpense = MainActivity.getCategoriesExpense();
        selectedMebers = new ArrayList<>();

        for(int i = 0; i < categoriesExpense.size(); i++){
            RadioButton rdbtn = new RadioButton(CreateExpenseActivity.this);
            rdbtn.setId(categoriesExpense.get(i).getId());
            rdbtn.setText(categoriesExpense.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            categoryRg.addView(rdbtn);
            if(i == 0) {
                rdbtn.setChecked(true);
                selectedCategory = new Category(rdbtn.getId(), categoriesExpense.get(i).getName());
            }
        }

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            selectedCategory = new Category(checkedId, radioText);
        });

        for(int i = 0; i < members.size(); i++){
            CheckBox cb = new CheckBox(CreateExpenseActivity.this);
            cb.setId(members.get(i).getUserId());
            cb.setText(members.get(i).getLogin());
            cb.setTextAppearance(R.style.simple_label);
            cb.setTextSize(18);
            cb.setButtonDrawable(R.drawable.cb_simple);
            membersCb.addView(cb);

            cb.setOnClickListener(v -> {
                if(cb.isChecked()) selectedMebers.add(cb.getText().toString());
                   else selectedMebers.remove(cb.getText().toString());
            });
        }

        createExpenseBtn.setOnClickListener(v -> {
            if(expenseNameEt.getText().toString().length()==0) expenseNameEt.setError("Wpisz nazwe wydatku!");
                else if(expenseCostEt.getText().toString().length()==0) expenseCostEt.setError("Wpisz kwote wydatku!");
                else if(selectedMebers.size()==0) Toast.makeText(CreateExpenseActivity.this, "Wybierz osoby dla których zrobiony jest wydatek", Toast.LENGTH_LONG).show();
                else {
                Expense expense = new Expense(expenseNameEt.getText().toString(), null, Double.parseDouble(expenseCostEt.getText().toString()), selectedCategory);
                ExpenseHolder expenseHolder = new ExpenseHolder(expense, selectedMebers);
                expenseService.createExpense(accessToken, walletId, expenseHolder);
                finish();
            }
        });


    }
}