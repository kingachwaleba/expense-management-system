package com.example.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.service.AccountService;
import java.io.IOException;

public class EditProfileActivity extends BaseActivity {

    Button changePasswordBtn, chooseImageBtn, saveChangeBtn, deleteAccountBtn;
    AccountService accountService;
    Uri selectedImage;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        saveChangeBtn = findViewById(R.id.save_change_btn);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        deleteAccountBtn = findViewById(R.id.delete_account_btn);
        imageView = findViewById(R.id.profile_image_iv);

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
            if(selectedImage!=null)
                accountService.uploadProfileImage(selectedImage);
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}