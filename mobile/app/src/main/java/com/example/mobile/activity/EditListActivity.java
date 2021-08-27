package com.example.mobile.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile.R;
import com.example.mobile.service.ListService;

public class EditListActivity extends AppCompatActivity {

    EditText newNameListEt;
    Button saveChangeBtn;
    String oldName, accessToken;
    int listId;
    ListService listService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        newNameListEt = findViewById(R.id.new_name_list_et);
        saveChangeBtn = findViewById(R.id.update_list_btn);
        oldName = getIntent().getStringExtra("oldListName");
        accessToken = getIntent().getStringExtra("accessToken");
        listId = getIntent().getIntExtra("listId", 0);
        newNameListEt.setText(oldName);
        listService = new ListService(this);

        saveChangeBtn.setOnClickListener(v -> {
            if(newNameListEt.getText().toString().length()>0){
                listService.editListName(accessToken, listId, newNameListEt.getText().toString());
                finish();
            } else Toast.makeText(EditListActivity.this, "Wpisz nazwę list", Toast.LENGTH_SHORT);
        });
    }
}