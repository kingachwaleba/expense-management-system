package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mobile.model.Category;
import com.example.mobile.service.ValidationTableService;

import java.util.List;

public class CreateWalletActivity extends AppCompatActivity {

    List<Category> categoriesForWallet;
    RadioGroup category_RG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        category_RG = findViewById(R.id.category_RG);

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getCategories(new ValidationTableService.OnValidationTable() {
            @Override
            public void onCategories(List<Category> categories) {
                categoriesForWallet = categories;
             /*   for(int i = 0; i < categoriesForWallet.size(); i++){
                    RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(categoriesForWallet.get(i).getName());
                    rdbtn.setTextAppearance(R.style.label);
                    rdbtn.setTextSize(18);
                    ViewGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
                    category_RG.addView(rdbtn,rprms);
                }*/
            }
        });

        for(int i = 0; i < 5; i++){
            RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText("Radio" + String.valueOf(i));
            rdbtn.setTextAppearance(R.style.label);
            rdbtn.setTextSize(18);
            ViewGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
            category_RG.addView(rdbtn,rprms);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}