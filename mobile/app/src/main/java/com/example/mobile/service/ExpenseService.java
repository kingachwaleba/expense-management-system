package com.example.mobile.service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;

import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.DebtsHolder;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

    public interface OnExpensesCallback{
        void onAllExpenses(List<Expense> expenses);
    }

    public interface OnExpenseByIdCallback{
        void onExpense(ExpenseHolder expense);
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
        Call<ExpenseHolder> call = apiInterface.getExpenseById("Bearer " + accessToken, id);
        call.enqueue(new Callback<ExpenseHolder>() {
            @Override
            public void onResponse(@NotNull Call<ExpenseHolder> call, @NotNull Response<ExpenseHolder> response) {
                callback.onExpense(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<ExpenseHolder> call, @NotNull Throwable t) {
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

    public void sendDebtNotification(String accessToken, int id, DebtsHolder debtsHolder) {
        Call<ResponseBody> call = apiInterface.sendDebtNotification("Bearer " + accessToken, id, debtsHolder);
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

    public void editExpenseById(String accessToken, int id, ExpenseHolder expense) {
        Call<ResponseBody> call = apiInterface.editExpenseById("Bearer " + accessToken, id, expense);
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

   /* public void uploadReceiptImage(Uri fileUri) {

        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        int startType = file.getPath().lastIndexOf('.');
        String type = file.getPath().substring(startType+1);

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/"+type), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        System.out.println(type);

        ApiInterface apiInterface = new ApiClient().getService();

        //creating a call and calling the upload image method
        Call<String> call = apiInterface.uploadProfileImage("Bearer " + session.getUserDetails().get(SessionManager.KEY_TOKEN), body);
        System.out.println(session.getUserDetails().get(SessionManager.KEY_TOKEN));
        //finally performing the call
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }*/
}
