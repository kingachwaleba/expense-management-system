package com.example.mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile.R;

import android.os.Bundle;
import android.widget.ImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profile_image);
        profileImage.setClipToOutline(true);
    }
}