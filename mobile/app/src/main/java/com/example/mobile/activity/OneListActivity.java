package com.example.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.config.SessionManager;
import com.example.mobile.model.Product;
import com.example.mobile.model.Unit;
import com.example.mobile.model.User;
import com.example.mobile.service.ListService;
import com.example.mobile.service.adapter.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OneListActivity extends BaseActivity {

    static Boolean ifEdit;
    static int productEditId;
    TextView nameListTv;
    EditText nameItemEt, quantityItemEt;
    Button addItemBtn, deleteListShopBtn, editListBtn, whoTakeListBtn, cancelItemBtn;
    RadioGroup unitRg;
    CheckBox personListCb, listCb;
    RecyclerView listItemRv;
    Unit unit;
    ListService listService;
    SessionManager session;
    String accessToken, login;
    int listId, firstRadioButton;
    List<Unit> units;

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
        cancelItemBtn = findViewById(R.id.cancel_product_btn);
        deleteListShopBtn = findViewById(R.id.delete_list_btn);
        editListBtn = findViewById(R.id.edit_list_btn);
        listItemRv = findViewById(R.id.list_item_rv);
        unitRg = findViewById(R.id.unit_RG);
        personListCb = findViewById(R.id.take_list_cb);
        listCb = findViewById(R.id.taken_list_cb);
        whoTakeListBtn = findViewById(R.id.who_take_list_btn);

        List<Product> productsInit = new ArrayList<>();
        listItemRv.setLayoutManager(new LinearLayoutManager(OneListActivity.this));
        ListItemAdapter listItemAdapterInit = new ListItemAdapter(this, productsInit, accessToken, login, (nameProduct, quantityProduct, unit, id) -> {
            nameItemEt.setText(nameProduct);
            quantityItemEt.setText(quantityProduct);
            unitRg.check(unit.getId());
            ifEdit = true;
            productEditId = id;
        });
        listItemRv.setAdapter(listItemAdapterInit);

        addItemBtn.setOnClickListener(v -> {
            Double quantityD = validateQuantity(quantityItemEt.getText().toString());
            if (nameItemEt.getText().toString().length() == 0)
                nameItemEt.setError("Podaj nazwe produktu");
            else if (quantityD == 0)
                quantityItemEt.setError("Wpisz poprawną ilość produktu!");
            else {
                Product product = new Product(nameItemEt.getText().toString(), quantityD, unit);
                if (!ifEdit) {
                    listService.addListItem(accessToken, listId, product);
                } else {
                    listService.editListItem(accessToken, productEditId, product);
                    ifEdit = false;
                }
                unitRg.check(firstRadioButton);
                nameItemEt.setText("");
                quantityItemEt.setText("");
                finish();
                startActivity(getIntent());
            }
        });

        cancelItemBtn.setOnClickListener(v -> {
            unitRg.check(firstRadioButton);
            nameItemEt.setText("");
            quantityItemEt.setText("");
            ifEdit = false;
        });

        deleteListShopBtn.setOnClickListener(v -> listService.deleteList(accessToken, listId));

        editListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OneListActivity.this, EditListActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("listId", listId);
            intent.putExtra("oldListName", nameListTv.getText().toString());
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemClock.sleep(200);
        initView();
    }

    protected void initView() {

        listService.getListById(listShop -> {
            nameListTv.setText(listShop.getName());
            ListItemAdapter listItemAdapter = new ListItemAdapter(OneListActivity.this, listShop.getListDetailSet(), accessToken, login, (nameProduct, quantityProduct, unit, id) -> {
                nameItemEt.setText(nameProduct);
                quantityItemEt.setText(quantityProduct);
                unitRg.check(unit.getId());
                ifEdit = true;
                productEditId = id;
            });
            listItemRv.setAdapter(listItemAdapter);

            if (listShop.getUser() != null) {
                personListCb.setChecked(true);
                personListCb.setEnabled(listShop.getUser().getLogin().equals(login));
                whoTakeListBtn.setVisibility(View.VISIBLE);
            } else {
                personListCb.setEnabled(true);
                personListCb.setChecked(false);
                whoTakeListBtn.setVisibility(View.INVISIBLE);
            }

            if (listShop.getStatus().getId() == MainActivity.getStatusByName("zrealizowany").getId()) {
                listCb.setChecked(true);
                personListCb.setEnabled(false);
            } else {
                listCb.setChecked(false);
            }

            whoTakeListBtn.setOnClickListener(v -> Toast.makeText(this, "To kupi " + listShop.getUser().getLogin(), Toast.LENGTH_SHORT).show());

            personListCb.setOnClickListener(v -> {
                if (listShop.getUser() == null) {
                    listShop.setUser(new User(login));
                    whoTakeListBtn.setVisibility(View.VISIBLE);
                    listService.changeListStatus(accessToken, listId, MainActivity.getStatusByName("zarezerwowany").getId());
                    for (int i = 0; i < listShop.getListDetailSet().size(); i++) {
                        if (!listShop.getListDetailSet().get(i).getStatus().getName().equals("zrealizowany")) {
                            listShop.getListDetailSet().get(i).setStatus(MainActivity.getStatusByName("zarezerwowany"));
                            listShop.getListDetailSet().get(i).setUser(new User(login));
                        }
                    }
                } else if (listShop.getUser().getLogin().equals(login)) {
                    listShop.setUser(null);
                    whoTakeListBtn.setVisibility(View.INVISIBLE);
                    listService.changeListStatus(accessToken, listId, MainActivity.getStatusByName("oczekujący").getId());
                }
                listItemAdapter.notifyDataSetChanged();
            });

            listCb.setOnClickListener(v -> {
                if (listShop.getStatus().getId() == MainActivity.getStatusByName("oczekujący").getId()) {
                    listShop.setUser(new User(login));
                    listShop.setStatus(MainActivity.getStatusByName("zrealizowany"));
                    personListCb.setChecked(true);
                    personListCb.setEnabled(false);
                    whoTakeListBtn.setVisibility(View.VISIBLE);
                    listService.changeListStatus(accessToken, listId, MainActivity.getStatusByName("zrealizowany").getId());
                    for (int i = 0; i < listShop.getListDetailSet().size(); i++) {
                        if (!listShop.getListDetailSet().get(i).getStatus().getName().equals("zrealizowany")) {
                            listShop.getListDetailSet().get(i).setStatus(MainActivity.getStatusByName("zrealizowany"));
                            listShop.getListDetailSet().get(i).setUser(new User(login));
                        }
                    }
                } else if (listShop.getStatus().getId() == MainActivity.getStatusByName("zarezerwowany").getId()) {
                    listShop.setUser(new User(login));
                    listShop.setStatus(MainActivity.getStatusByName("zrealizowany"));
                    listService.changeListStatus(accessToken, listId, MainActivity.getStatusByName("zrealizowany").getId());
                    for (int i = 0; i < listShop.getListDetailSet().size(); i++) {
                        if (!listShop.getListDetailSet().get(i).getStatus().getName().equals("zrealizowany")) {
                            listShop.getListDetailSet().get(i).setStatus(MainActivity.getStatusByName("zrealizowany"));
                            listShop.getListDetailSet().get(i).setUser(new User(login));
                        }
                    }
                } else {
                    listShop.setUser(null);
                    personListCb.setChecked(false);
                    personListCb.setEnabled(true);
                    whoTakeListBtn.setVisibility(View.INVISIBLE);
                    listShop.setStatus(MainActivity.getStatusByName("oczekujący"));
                    listService.changeListStatus(accessToken, listId, MainActivity.getStatusByName("oczekujący").getId());
                }
                listItemAdapter.notifyDataSetChanged();
            });

        }, accessToken, listId);

        units = MainActivity.getProductUnits();
        unitRg.removeAllViews();
        for (int i = 0; i < units.size(); i++) {
            RadioButton rdbtn = new RadioButton(OneListActivity.this);
            rdbtn.setId(units.get(i).getId());
            rdbtn.setText(units.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            unitRg.addView(rdbtn);
            if (i == 0) {
                rdbtn.setChecked(true);
                unit = new Unit(units.get(i).getId(), units.get(i).getName());
            }
        }
        firstRadioButton = unitRg.getCheckedRadioButtonId();

        unitRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            String radioText = rb.getText().toString();
            unit = new Unit(checkedId, radioText);
        });
    }

    public double validateQuantity(String cost) {
        if (Pattern.compile("^\\d{0,5}(\\.\\d{1,2})?$").matcher(cost).matches()) {
            try {
                return Double.parseDouble(cost);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else return 0;
    }
}