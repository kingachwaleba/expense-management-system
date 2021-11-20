package com.example.mobile.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.example.mobile.ImageHelper;
import com.example.mobile.activity.WalletActivity;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.ErrorUtils;
import com.example.mobile.model.DebtsHolder;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void getAllExpenses(ExpenseService.OnExpensesCallback callback, String accessToken, int id) {
        Call<List<Expense>> call = apiInterface.getAllExpense("Bearer " + accessToken, id);
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(@NotNull Call<List<Expense>> call, @NotNull Response<List<Expense>> response) {
                callback.onAllExpenses(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Expense>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getExpenseById(ExpenseService.OnExpenseByIdCallback callback, String accessToken, int id) {
        Call<ExpenseHolder> call = apiInterface.getExpenseById("Bearer " + accessToken, id);
        call.enqueue(new Callback<ExpenseHolder>() {
            @Override
            public void onResponse(@NotNull Call<ExpenseHolder> call, @NotNull Response<ExpenseHolder> response) {
                callback.onExpense(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<ExpenseHolder> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void createExpense(String accessToken, int id, ExpenseHolder expenseHolder, int walletId) {
        Call<ResponseBody> call = apiInterface.createExpense("Bearer " + accessToken, id, expenseHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(context, WalletActivity.class);
                    i.putExtra("id", String.valueOf(walletId));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    (context).startActivity(i);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void uploadReceiptImage(Bitmap bitmap, String accessToken, String name, OnReceiptCallback callback) {

        //creating a file
        File file = ImageHelper.bitmapToFile(context, bitmap, name.replaceAll("\\s", "") + ".png");

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        ApiInterface apiInterface = new ApiClient().getService();

        //creating a call and calling the upload image method
        Call<ResponseBody> call = apiInterface.uploadReceiptImage("Bearer " + accessToken, body);
        //finally performing the call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        try {
                            callback.onReceipt(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                } else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editExpenseById(String accessToken, int id, ExpenseHolder expense, int walletId) {
        Call<ResponseBody> call = apiInterface.editExpenseById("Bearer " + accessToken, id, expense);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(context, WalletActivity.class);
                    i.putExtra("id", String.valueOf(walletId));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    (context).startActivity(i);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteExpense(String accessToken, int id, int walletId) {
        Call<ResponseBody> call = apiInterface.deleteExpense("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(context, WalletActivity.class);
                    i.putExtra("id", String.valueOf(walletId));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    (context).startActivity(i);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void payDebt(String accessToken, int id, DebtsHolder debtsHolder) {
        Call<ResponseBody> call = apiInterface.payDebt("Bearer " + accessToken, id, debtsHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(context, WalletActivity.class);
                    i.putExtra("id", String.valueOf(id));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    (context).startActivity(i);
                } else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void sendDebtNotification(String accessToken, int id, DebtsHolder debtsHolder) {
        Call<ResponseBody> call = apiInterface.sendDebtNotification("Bearer " + accessToken, id, debtsHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful())
                    Toast.makeText(context, "Przypomnienie o długu zostało wysłane", Toast.LENGTH_SHORT).show();
                else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public interface OnExpensesCallback {
        void onAllExpenses(List<Expense> expenses);
    }

    public interface OnExpenseByIdCallback {
        void onExpense(ExpenseHolder expense);
    }

    public interface OnReceiptCallback {
        void onReceipt(String path);
    }
}
