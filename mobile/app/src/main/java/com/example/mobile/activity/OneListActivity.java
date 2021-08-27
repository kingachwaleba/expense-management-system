package com.example.mobile.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Product;
import com.example.mobile.model.Unit;
import com.example.mobile.service.ListService;
import com.example.mobile.service.ValidationTableService;
import com.example.mobile.service.adapter.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class OneListActivity extends AppCompatActivity {

    TextView nameListTv;
    static EditText nameItemEt, quantityItemEt;
    Button addItemBtn, deleteListShopBtn;
    static RadioGroup unitRg;
    Unit unit;
    int firstRadioButton;
    RecyclerView listItemRv;
    ListService listService;
    SessionManager session;
    String accessToken, login;
    int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_list);

        session = new SessionManager(this);

        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        login = session.getUserDetails().get(SessionManager.KEY_LOGIN);
        listService = new ListService(this);
        listId = getIntent().getIntExtra("listId", 0);

        nameListTv = findViewById(R.id.list_name_tv);
        nameItemEt = findViewById(R.id.name_prodcut_et);
        quantityItemEt = findViewById(R.id.quantity_et);
        addItemBtn = findViewById(R.id.add_product_btn);
        deleteListShopBtn = findViewById(R.id.delete_list_btn);
        listItemRv = findViewById(R.id.list_item_rv);
        unitRg = findViewById(R.id.unit_RG);

        List<Product> productsInit = new ArrayList<>();
        listItemRv.setLayoutManager(new LinearLayoutManager(OneListActivity.this));
        ListItemAdapter listItemAdapterInit = new ListItemAdapter(this, productsInit, accessToken, login);
        listItemRv.setAdapter(listItemAdapterInit);

        listService.getListById(listShop -> {
            nameListTv.setText(listShop.getName());
            ListItemAdapter listItemAdapter= new ListItemAdapter(OneListActivity.this, listShop.getListDetailSet(), accessToken, login);
            listItemRv.setAdapter(listItemAdapter);
        }, accessToken, listId);




        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getUnits(units -> {
            for(int i = 0; i < units.size(); i++){
                RadioButton rdbtn = new RadioButton(OneListActivity.this);
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

        addItemBtn.setOnClickListener(v -> {
            if(nameItemEt.getText().toString().length()>0 && quantityItemEt.getText().toString().length()>0){
                Product product = new Product(nameItemEt.getText().toString(), Double.parseDouble(quantityItemEt.getText().toString()), unit);
                unitRg.check(firstRadioButton);

            } else Toast.makeText(OneListActivity.this, "Wprowadź nazwe i ilość produktu", Toast.LENGTH_SHORT).show();
        });

        deleteListShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static void setNameQuantityProductEt(String name, String quantity, Unit unit){
        nameItemEt.setText(name);
        quantityItemEt.setText(quantity);
        unitRg.check(unit.getId());
    }
}