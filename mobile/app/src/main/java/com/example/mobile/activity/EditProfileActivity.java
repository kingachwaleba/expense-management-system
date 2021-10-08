package com.example.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.service.AccountService;
import java.io.IOException;

public class EditProfileActivity extends BaseActivity {

    Button changePasswordBtn, chooseImageBtn, saveChangeBtn, deleteAccountBtn, rotateLeftBtn, rotateRightBtn;
    AccountService accountService;
    Uri selectedImage;
    ImageView imageView;
    LinearLayout imagePreviewL;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        saveChangeBtn = findViewById(R.id.save_change_btn);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        deleteAccountBtn = findViewById(R.id.delete_account_btn);
        imageView = findViewById(R.id.profile_image_iv);
        imagePreviewL = findViewById(R.id.image_preview_l);
        rotateLeftBtn = findViewById(R.id.rotate_left_btn);
        rotateRightBtn = findViewById(R.id.rotate_right_btn);

        session = new SessionManager(this);
        accountService = new AccountService(this);

        chooseImageBtn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 100);
        });

        changePasswordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        saveChangeBtn.setOnClickListener(v -> {
            if (selectedImage != null){
                try{
                    int max = 200;
                    int factor;
                    int newHeigh, newWidth;
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    if(bitmap.getHeight() > bitmap.getWidth()){
                        factor = bitmap.getHeight() / max;
                        newHeigh = max;
                        newWidth = bitmap.getWidth()/factor;
                    } else {
                        factor = bitmap.getWidth() / max;
                        newWidth = max;
                        newHeigh = bitmap.getHeight()/factor;
                    }

                    accountService.uploadProfileImage(Bitmap.createScaledBitmap(bitmap,newWidth, newHeigh, true), selectedImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
               // a
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        rotateRightBtn.setOnClickListener(v -> {
            new Thread(() -> setProfileImagePreview(90)).start();
            imageView.setImageBitmap(imageBitmap);
        });

        rotateLeftBtn.setOnClickListener(v -> {
            new Thread(() -> setProfileImagePreview(-90)).start();
            imageView.setImageBitmap(imageBitmap);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);
            imagePreviewL.setVisibility(View.VISIBLE);
        }
    }

    public void setProfileImagePreview(int degree){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        imageBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
       // return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}