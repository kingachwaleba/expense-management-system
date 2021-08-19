package com.example.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mobile.model.Category;
import com.example.mobile.model.Member;
import com.example.mobile.model.Wallet;
import com.example.mobile.service.UserService;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.WalletService;

import java.util.ArrayList;
import java.util.List;

public class CreateWalletActivity extends AppCompatActivity {

    RadioGroup category_RG;
    EditText name, description, search;
    List<String> addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.pagename);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.nameEdit);
        description = findViewById(R.id.descriptionEdit);
        search = findViewById(R.id.memberEdit);

        ViewGroup categoryButtonLayout = (ViewGroup) findViewById(R.id.category_RG);


        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                WalletService walletService = new WalletService(getParent());
                if(!search.getText().toString().equals("")){
                    walletService.getMembersByInfix(new WalletService.OnMemberSearchCallback() {
                        @Override
                        public void onMembersList(List<String> members) {
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("members_search", (ArrayList<String>) members);
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container_view_search, UserSearchFragment.class, bundle)
                                    .commit();
                        }
                    }, search.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getCategories(new ValidationTableService.OnValidationTable() {
            @Override
            public void onCategories(List<Category> categories) {
                for(int i = 0; i < categories.size(); i++){
                    RadioButton rdbtn = new RadioButton(CreateWalletActivity.this);
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(categories.get(i).getName());
                    rdbtn.setTextAppearance(R.style.label);
                    rdbtn.setTextSize(18);
                    rdbtn.setButtonDrawable(R.drawable.radio_button);
                    categoryButtonLayout.addView(rdbtn);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}