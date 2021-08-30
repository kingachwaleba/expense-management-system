package com.example.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile.R;
import com.example.mobile.model.ListCreate;
import com.example.mobile.model.ListShop;
import com.example.mobile.model.Product;
import com.example.mobile.model.Unit;
import com.example.mobile.service.ListService;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.adapter.ProductCreateListAdapter;
import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends BaseActivity {

    String accessToken;
    int walletId;
    Unit unit;
    ListService listService;

    Button addProductBtn, createListBtn, cancelListBtn;
    EditText nameListEt, nameProductEt, quantityProductEt;
    RadioGroup unitRg;
    RecyclerView addedProductsRv;
    List<Product> productList;
    ProductCreateListAdapter productCreateListAdapter;
    int firstRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        accessToken = getIntent().getStringExtra("accessToken");
        walletId = getIntent().getIntExtra("walletId", 0);
        listService = new ListService(this);

        addProductBtn = findViewById(R.id.add_product_btn);
        createListBtn = findViewById(R.id.create_list_btn);
        nameListEt = findViewById(R.id.name_list_et);
        nameProductEt = findViewById(R.id.name_prodcut_et);
        quantityProductEt = findViewById(R.id.quantity_et);
        unitRg = findViewById(R.id.unit_RG);
        cancelListBtn = findViewById(R.id.cancel_list_btn);

        addedProductsRv = findViewById(R.id.added_products_rv);
        addedProductsRv.setLayoutManager(new LinearLayoutManager(CreateListActivity.this));
        productList = new ArrayList<>();
        productCreateListAdapter = new ProductCreateListAdapter(this, productList, (nameProduct, quantityProduct, unit, id) -> {
            nameProductEt.setText(nameProduct);
            quantityProductEt.setText(quantityProduct);
            unitRg.check(unit.getId());
        });
        addedProductsRv.setAdapter(productCreateListAdapter);


        cancelListBtn.setOnClickListener(v -> finish());

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getUnits(units -> {
            for(int i = 0; i < units.size(); i++){
                RadioButton rdbtn = new RadioButton(CreateListActivity.this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(units.get(i).getName());
                rdbtn.setTextAppearance(R.style.simple_label);
                rdbtn.setTextSize(18);
                rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
                unitRg.addView(rdbtn);
                if(i == 0) rdbtn.setChecked(true);
            }
            firstRadioButton = unitRg.getCheckedRadioButtonId();
        });

        unitRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb=findViewById(checkedId);
            String radioText = rb.getText().toString();
            unit = new Unit(checkedId, radioText);
        });

        addProductBtn.setOnClickListener(v -> {
            if(nameProductEt.getText().toString().length()==0) nameProductEt.setError("Wprowadź nazwe produktu!");
                else if(quantityProductEt.getText().toString().length()==0) quantityProductEt.setError("Wprowadź ilość produku!");
                    else {
                        productList.add(new Product(nameProductEt.getText().toString(), Double.parseDouble(quantityProductEt.getText().toString()), unit));
                        productCreateListAdapter.notifyDataSetChanged();
                        nameProductEt.setText("");
                        quantityProductEt.setText("");
                        unitRg.check(firstRadioButton);
                    }
        });

        createListBtn.setOnClickListener(v -> {
            if(nameListEt.getText().toString().length()>0){
                listService.createList(accessToken, walletId, new ListCreate(new ListShop(nameListEt.getText().toString()), productList));
                finish();
            } else nameListEt.setError("Podaj nazwe listy zakupów!");
        });
    }
}