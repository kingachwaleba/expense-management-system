package com.example.mobile.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.activity.ExpenseActivity;
import com.example.mobile.model.Expense;
import com.example.mobile.model.User;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder>{
    private final List<Expense> mExpenses;
    private final LayoutInflater mInflater;
    private final String mAccessToken;
    private final List<User> mAllMembers;

    public ExpensesAdapter(Context context, List<Expense> expenses, String accessToken, List<User> allMembers){
        mExpenses = expenses;
        mInflater = LayoutInflater.from(context);
        mAccessToken = accessToken;
        mAllMembers = allMembers;
    }

    @Override
    public @NotNull ExpensesAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.item_expense, parent, false);
        return new ExpensesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpensesAdapter.ViewHolder holder, int position) {
        Expense expense = mExpenses.get(position);
        String expenseOwner = mInflater.getContext().getResources().getString(R.string.who_make_label) + " " +  expense.getUser().getLogin();
        String expenseCost = mInflater.getContext().getResources().getString(R.string.cost_label) + " " + expense.getTotal_cost();
        String expenseCategory = mInflater.getContext().getResources().getString(R.string.category_label) + " " + expense.getCategory().getName();

        holder.expenseNameTv.setText(expense.getName());
        holder.expenseOwnerTv.setText(expenseOwner);
        holder.expenseCostTv.setText(expenseCost);
        holder.expenseCategoryTv.setText(expenseCategory);

        holder.goToExpenseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ExpenseActivity.class);
            intent.putExtra("expenseId", expense.getId());
            intent.putExtra("accessToken", mAccessToken);
            intent.putParcelableArrayListExtra("allMembers", (ArrayList<? extends Parcelable>) mAllMembers);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTv, expenseOwnerTv, expenseCategoryTv, expenseCostTv, goToExpenseBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            expenseNameTv = itemView.findViewById(R.id.name_expense_tv);
            expenseOwnerTv = itemView.findViewById(R.id.who_make_tv);
            expenseCategoryTv = itemView.findViewById(R.id.category_expanse_tv);
            expenseCostTv = itemView.findViewById(R.id.cost_expense_tv);
            goToExpenseBtn = itemView.findViewById(R.id.go_to_expense_tv);
        }
    }
}
