package com.example.mobile.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.model.DebtsHolder;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseService {
    Context context;
    ApiInterface apiInterface;

    public ExpenseService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnExpensesCallback{
        void onAllExpenses(List<Expense> expenses);
    }

    public interface OnExpenseByIdCallback{
        void onExpense(Expense expense);
    }

    public void getAllExpenses(ExpenseService.OnExpensesCallback callback, String accessToken, int id){
        Call<List<Expense>> call = apiInterface.getAllExpense("Bearer " + accessToken, id);
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(@NotNull Call<List<Expense>> call, @NotNull Response<List<Expense>> response) {
                callback.onAllExpenses(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Expense>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getExpenseById(ExpenseService.OnExpenseByIdCallback callback, String accessToken, int id){
        Call<Expense> call = apiInterface.getExpenseById("Bearer " + accessToken, id);
        call.enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(@NotNull Call<Expense> call, @NotNull Response<Expense> response) {
                callback.onExpense(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Expense> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void createExpense(String accessToken, int id, ExpenseHolder expenseHolder) {
        Call<ResponseBody> call = apiInterface.createExpense("Bearer " + accessToken, id, expenseHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteExpense(String accessToken, int id) {
        Call<ResponseBody> call = apiInterface.deleteExpense("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void payDebt(String accessToken, int id, DebtsHolder debtsHolder) {
        Call<ResponseBody> call = apiInterface.payDebt("Bearer " + accessToken, id, debtsHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
