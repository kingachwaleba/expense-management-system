package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Button addItemBtn, deleteListShopBtn, editListBtn;
    static RadioGroup unitRg;
    Unit unit;
    int firstRadioButton;
    RecyclerView listItemRv;
    ListService listService;
    SessionManager session;
    String accessToken, login;
    int listId;
    static Boolean ifEdit;
    static int productEditId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_list);

        session = new SessionManager(this);

        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
        login = session.getUserDetails().get(SessionManager.KEY_LOGIN);
        listService = new ListService(this);
        listId = getIntent().getIntExtra("listId", 0);
        ifEdit = false;

        nameListTv = findViewById(R.id.list_name_tv);
        nameItemEt = findViewById(R.id.name_prodcut_et);
        quantityItemEt = findViewById(R.id.quantity_et);
        addItemBtn = findViewById(R.id.add_product_btn);
        deleteListShopBtn = findViewById(R.id.delete_list_btn);
        editListBtn = findViewById(R.id.edit_list_btn);
        listItemRv = findViewById(R.id.list_item_rv);
        unitRg = findViewById(R.id.unit_RG);

        List<Product> productsInit = new ArrayList<>();
        listItemRv.setLayoutManager(new LinearLayoutManager(OneListActivity.this));
        ListItemAdapter listItemAdapterInit = new ListItemAdapter(this, productsInit, accessToken, login);
        listItemRv.setAdapter(listItemAdapterInit);

        addItemBtn.setOnClickListener(v -> {
            if(nameItemEt.getText().toString().length()>0 && quantityItemEt.getText().toString().length()>0){
                Product product = new Product(nameItemEt.getText().toString(), Double.parseDouble(quantityItemEt.getText().toString()), unit);
                if(!ifEdit){
                    listService.addListItem(accessToken, listId, product);
                } else {
                    listService.editListItem(accessToken, productEditId, product);
                    ifEdit = true;
                }
                unitRg.check(firstRadioButton);
                nameItemEt.setText("");
                quantityItemEt.setText("");
                finish();
                startActivity(getIntent());
            } else Toast.makeText(OneListActivity.this, "Wprowadź nazwe i ilość produktu", Toast.LENGTH_SHORT).show();
        });

        deleteListShopBtn.setOnClickListener(v -> {
        });

        editListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OneListActivity.this, EditListActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("listId", listId);
            intent.putExtra("oldListName", nameListTv.getText().toString());
            startActivity(intent);
        });
    }

    public static void setNameQuantityProductEt(String name, String quantity, Unit unit, int itemEditId){
        nameItemEt.setText(name);
        quantityItemEt.setText(quantity);
        unitRg.check(unit.getId());
        ifEdit = true;
        productEditId = itemEditId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemClock.sleep(200);
        initView();
    }

    protected void initView(){
        listService.getListById(listShop -> {
            nameListTv.setText(listShop.getName());
            ListItemAdapter listItemAdapter= new ListItemAdapter(OneListActivity.this, listShop.getListDetailSet(), accessToken, login);
            listItemRv.setAdapter(listItemAdapter);
        }, accessToken, listId);

        ValidationTableService validationTableService = new ValidationTableService(this);
        validationTableService.getUnits(units -> {
            for(int i = 0; i < units.size(); i++){
                RadioButton rdbtn = new RadioButton(OneListActivity.this);
                rdbtn.setId(units.get(i).getId());
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
    }

}