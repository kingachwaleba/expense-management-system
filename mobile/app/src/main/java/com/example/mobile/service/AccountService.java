package com.example.mobile.service;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.loader.content.CursorLoader;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Invitation;
import com.example.mobile.model.Message;
import com.example.mobile.model.UpdatePasswordHolder;
import com.example.mobile.model.User;
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

public class AccountService {
    Context context;
    ApiInterface apiInterface;
    SessionManager session;

    public AccountService(Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
        this.session = new SessionManager(context);
    }

    public interface OnAccountCallback{
        void onMyAccount(User user);
    }

    public interface OnInvitationCallback{
        void onAllInvitations(List<Invitation> invitations);
    }

    public interface OnNotificationCallback{
        void onAllNotifications(List<Message> messages);
    }

    public void getAccount(OnAccountCallback callback, String accessToken){
        Call<User> call = apiInterface.getAccount("Bearer " + accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                callback.onMyAccount(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getInvitations(OnInvitationCallback callback, String accessToken){
        Call<List<Invitation>> call = apiInterface.getInvitations("Bearer " + accessToken);
        call.enqueue(new Callback<List<Invitation>>() {
            @Override
            public void onResponse(@NotNull Call<List<Invitation>> call, @NotNull Response<List<Invitation>> response) {
                callback.onAllInvitations(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Invitation>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void manageInvitation(String accessToken, int id, Boolean flag){
        Call<ResponseBody> call = apiInterface.manageInvitation("Bearer " + accessToken, id, flag);
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

    public void changePassword(String accessToken, UpdatePasswordHolder updatePasswordHolder){
        Call<ResponseBody> call = apiInterface.changePassword("Bearer " + accessToken, updatePasswordHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.body()!=null)
                    ((Activity)context).finish();
                else Toast.makeText(context,"Nieprawidłowe dane",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }


    public void deleteNotification(String accessToken, int id){
        Call<ResponseBody> call = apiInterface.deleteNotification("Bearer " + accessToken, id);
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

    public void getDebtNotification(OnNotificationCallback callback, String accessToken){
        Call<List<Message>> call = apiInterface.getDebtNotification("Bearer " + accessToken);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NotNull Call<List<Message>> call, @NotNull Response<List<Message>> response) {
                callback.onAllNotifications(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Message>> call, @NotNull Throwable t) {
                Toast.makeText(context,"Coś poszło nie tak",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void uploadProfileImage(Uri fileUri) {




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
    }
}
