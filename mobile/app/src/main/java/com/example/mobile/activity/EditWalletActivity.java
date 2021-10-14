package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.mobile.R;
import com.example.mobile.model.Category;
import com.example.mobile.model.WalletCreate;
import com.example.mobile.service.WalletService;
import java.util.List;

public class EditWalletActivity extends BaseActivity {

    EditText nameEt, descriptionEt;
    RadioGroup categoryRg;
    String accessToken;
    Category category;
    Button updateWalletBtn, cancelBtn;
    WalletService walletService;
    int walletId;
    String walletName, walletDescription, walletCategory;
    List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        walletName = getIntent().getStringExtra("walletName");
        walletDescription = getIntent().getStringExtra("walletDescription");
        walletCategory = getIntent().getStringExtra("walletCategory");

        walletService = new WalletService(this);

        accessToken = getIntent().getStringExtra("accessToken");
        walletId = getIntent().getIntExtra("walletId",0);
        categoryRg = findViewById(R.id.category_RG);
        nameEt = findViewById(R.id.name_et);
        descriptionEt = findViewById(R.id.description_et);
        updateWalletBtn = findViewById(R.id.update_wallet_btn);
        cancelBtn = findViewById(R.id.cancel_wallet_btn);

        nameEt.setText(walletName);
        descriptionEt.setText(walletDescription);

        categories = MainActivity.getCategoriesWallet();
        for(int i = 0; i < categories.size(); i++){
            RadioButton rdbtn = new RadioButton(EditWalletActivity.this);
            rdbtn.setId(categories.get(i).getId());
            rdbtn.setText(categories.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            categoryRg.addView(rdbtn);
            if(categories.get(i).getName().equals(walletCategory)){
                rdbtn.setChecked(true);
                category = new Category(categories.get(i).getId(), categories.get(i).getName());
            }
        }

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            category = new Category(checkedId, radioText);
        });

        updateWalletBtn.setOnClickListener(v -> {
            String nameS = nameEt.getText().toString();
            if(validateName(nameS)){
                String descriptionS = descriptionEt.getText().toString();
                WalletCreate wallet = new WalletCreate(walletId, nameS, category,descriptionS);
                walletService.updateWallet(accessToken, walletId, wallet);
                finish();
            } else nameEt.setError("Podaj nazwę portfela");
        });

        cancelBtn.setOnClickListener(v -> finish());
    }

    public boolean validateName(String s){
        return s.length() > 0;
    }
}



