package com.example.mobile.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import com.example.mobile.ImageHelper;
import com.example.mobile.config.ApiClient;
import com.example.mobile.config.ApiInterface;
import com.example.mobile.config.ErrorUtils;
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

    public void getAccount(OnAccountCallback callback, String accessToken) {
        Call<User> call = apiInterface.getAccount("Bearer " + accessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                callback.onMyAccount(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void changePassword(String accessToken, UpdatePasswordHolder updatePasswordHolder) {
        Call<ResponseBody> call = apiInterface.changePassword("Bearer " + accessToken, updatePasswordHolder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful())
                    ((Activity) context).finish();
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

    public void uploadProfileImage(Bitmap bitmap) {
        File file = ImageHelper.bitmapToFile(context, bitmap,
                session.getUserDetails().get(SessionManager.KEY_LOGIN) + ".png");

        int startType = file.getPath().lastIndexOf('.');
        String type = file.getPath().substring(startType + 1);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/" + type), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(),
                requestFile);

        Call<String> call = apiInterface.uploadProfileImage("Bearer "
                + session.getUserDetails().get(SessionManager.KEY_TOKEN), body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response){
                if (response.isSuccessful()){
                    session.setKeyImagePathServer(response.body());
                    Toast.makeText(context, "Zdjęcie zostało zmienione",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteProfileImage() {
        Call<ResponseBody> call = apiInterface.deleteProfileImage("Bearer " + session.getUserDetails().get(SessionManager.KEY_TOKEN));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String error = ErrorUtils.parseError(response);
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Zdjęcie profilowe zostało usunięte", Toast.LENGTH_SHORT).show();
                    session.setKeyImagePathServer(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteAccount(String password) {
        Call<ResponseBody> call = apiInterface.deleteAccount("Bearer " + session.getUserDetails().get(SessionManager.KEY_TOKEN), password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful())
                    session.logoutUser();
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

    public void getInvitations(OnInvitationCallback callback, String accessToken) {
        Call<List<Invitation>> call = apiInterface.getInvitations("Bearer " + accessToken);
        call.enqueue(new Callback<List<Invitation>>() {
            @Override
            public void onResponse(@NotNull Call<List<Invitation>> call, @NotNull Response<List<Invitation>> response) {
                callback.onAllInvitations(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Invitation>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getDebtNotification(OnNotificationCallback callback, String accessToken) {
        Call<List<Message>> call = apiInterface.getDebtNotification("Bearer " + accessToken);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NotNull Call<List<Message>> call, @NotNull Response<List<Message>> response) {
                callback.onAllNotifications(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Message>> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void manageInvitation(String accessToken, int id, Boolean flag) {
        Call<ResponseBody> call = apiInterface.manageInvitation("Bearer " + accessToken, id, flag);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void deleteNotification(String accessToken, int id) {
        Call<ResponseBody> call = apiInterface.deleteNotification("Bearer " + accessToken, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(context, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public interface OnAccountCallback {
        void onMyAccount(User user);
    }

    public interface OnInvitationCallback {
        void onAllInvitations(List<Invitation> invitations);
    }

    public interface OnNotificationCallback {
        void onAllNotifications(List<Message> messages);
    }
}
