package com.example.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.service.AccountService;
import java.io.IOException;

import static javax.microedition.khronos.opengles.GL10.GL_MAX_TEXTURE_SIZE;

public class EditProfileActivity extends BaseActivity {

    Button changePasswordBtn, chooseImageBtn, saveChangeBtn, deleteImageBtn, deleteAccountBtn;
    ImageView imageView;

    AccountService accountService;
    Uri selectedImage;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        chooseImageBtn = findViewById(R.id.choose_image_btn);
        saveChangeBtn = findViewById(R.id.save_change_btn);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        deleteImageBtn = findViewById(R.id.delete_image_btn);
        deleteAccountBtn = findViewById(R.id.delete_account_btn);
        imageView = findViewById(R.id.new_profile_image_iv);

        session = new SessionManager(this);
        accountService = new AccountService(this);

        deleteAccountBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
        });

        chooseImageBtn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 100);
        });

        deleteImageBtn.setOnClickListener(v -> accountService.deleteProfileImage());

        changePasswordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        saveChangeBtn.setOnClickListener(v -> {
            if (selectedImage != null) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    accountService.uploadProfileImage(Bitmap.createScaledBitmap(bitmap, 300, 400, true));
                    imageView.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();

            BitmapFactory.Options bitMapOption = new BitmapFactory.Options();
            bitMapOption.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(ImageHelper.getRealPathFromURI(this, selectedImage), bitMapOption);

            if (bitMapOption.outWidth < GL_MAX_TEXTURE_SIZE && bitMapOption.outHeight < GL_MAX_TEXTURE_SIZE)
                try {
                    imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else
                Toast.makeText(this, "Wybierz zdjęcie o mniejszej rozdzielczości", Toast.LENGTH_SHORT).show();

            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}