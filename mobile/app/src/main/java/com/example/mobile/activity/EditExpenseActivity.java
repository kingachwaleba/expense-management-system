package com.example.mobile.activity;

import static javax.microedition.khronos.opengles.GL10.GL_MAX_TEXTURE_SIZE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mobile.ImageHelper;
import com.example.mobile.R;
import com.example.mobile.model.Category;
import com.example.mobile.model.Expense;
import com.example.mobile.model.ExpenseHolder;
import com.example.mobile.model.Member;
import com.example.mobile.model.User;
import com.example.mobile.service.ExpenseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EditExpenseActivity extends BaseActivity {

    RadioGroup categoryRg;
    LinearLayout membersCb;
    Button editExpenseBtn, cancelEditBtn, chooseImageBtn, deletePhotoBtn;
    ImageView receiptIv;
    EditText nameExpenseEt, costExpenseEt;

    ExpenseService expenseService;
    int expenseId, walletId;
    String nameExpense, costExpense, category, receiptPath, accessToken, imagePath;
    List<Member> selectedUser, allMembers;
    List<String> selectedUsersLogin;
    List<Category> categoriesExpense;
    Category selectedCategory;
    User expenseOwner;
    Uri selectedImage;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        accessToken = getIntent().getStringExtra("accessToken");
        walletId = getIntent().getIntExtra("walletId", 0);
        expenseId = getIntent().getIntExtra("expenseId", 0);
        nameExpense = getIntent().getStringExtra("nameExpense");
        costExpense = getIntent().getStringExtra("costExpense");
        category = getIntent().getStringExtra("categoryExpense");
        receiptPath = getIntent().getStringExtra("receipt");
        selectedUser = getIntent().getParcelableArrayListExtra("selectedUsers");
        allMembers = getIntent().getParcelableArrayListExtra("walletUsers");


        nameExpenseEt = findViewById(R.id.name_expense_et);
        costExpenseEt = findViewById(R.id.cost_expense_et);
        categoryRg = findViewById(R.id.category_RG);
        membersCb = findViewById(R.id.members_expense_l);
        receiptIv = findViewById(R.id.receipt_iv);
        chooseImageBtn = findViewById(R.id.choose_image_btn);

        editExpenseBtn = findViewById(R.id.edit_expense_btn);
        cancelEditBtn = findViewById(R.id.cancel_edit_expense_btn);
        deletePhotoBtn = findViewById(R.id.delete_photo_btn);

        expenseService = new ExpenseService(this);

        nameExpenseEt.setText(nameExpense);
        costExpenseEt.setText(costExpense);

        categoriesExpense = MainActivity.getCategoriesExpense();
        selectedUsersLogin = new ArrayList<>();

        if (receiptPath != null) {
            ImageHelper.downloadImage((picasso, urlBuilder) -> picasso.load(String.valueOf(urlBuilder)).into(receiptIv), getApplicationContext(), accessToken, receiptPath);
            receiptIv.setVisibility(View.VISIBLE);
            imagePath = receiptPath;
            deletePhotoBtn.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < selectedUser.size(); i++)
            selectedUsersLogin.add(selectedUser.get(i).getLogin());

        for (int i = 0; i < categoriesExpense.size(); i++) {
            RadioButton rdbtn = new RadioButton(EditExpenseActivity.this);
            rdbtn.setId(categoriesExpense.get(i).getId());
            rdbtn.setText(categoriesExpense.get(i).getName());
            rdbtn.setTextAppearance(R.style.simple_label);
            rdbtn.setTextSize(18);
            rdbtn.setButtonDrawable(R.drawable.rb_radio_button);
            categoryRg.addView(rdbtn);
            if (categoriesExpense.get(i).getName().equals(category)) {
                rdbtn.setChecked(true);
                selectedCategory = new Category(categoriesExpense.get(i).getId(), categoriesExpense.get(i).getName());
            }
        }

        categoryRg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            String radioText = rb.getText().toString();
            selectedCategory = new Category(checkedId, radioText);
        });

        for (int i = 0; i < allMembers.size(); i++) {
            CheckBox cb = new CheckBox(EditExpenseActivity.this);
            cb.setId(allMembers.get(i).getUserId());
            cb.setText(allMembers.get(i).getLogin());
            cb.setTextAppearance(R.style.simple_label);
            cb.setTextSize(18);
            cb.setButtonDrawable(R.drawable.cb_simple);
            membersCb.addView(cb);
            if (selectedUsersLogin.contains(allMembers.get(i).getLogin())) cb.setChecked(true);
            cb.setOnClickListener(v -> {
                if (cb.isChecked()) selectedUsersLogin.add(cb.getText().toString());
                else selectedUsersLogin.remove(cb.getText().toString());
            });
        }

        cancelEditBtn.setOnClickListener(v -> finish());

        deletePhotoBtn.setOnClickListener(v -> {
            imageBitmap = null;
            imagePath = null;
            receiptIv.setVisibility(View.GONE);
            deletePhotoBtn.setVisibility(View.GONE);
        });

        chooseImageBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                finish();
                startActivity(intent);
                return;
            }
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 100);
        });

        editExpenseBtn.setOnClickListener(v -> {
            Double costD = validateCost(costExpenseEt.getText().toString());

            if (nameExpenseEt.getText().toString().length() == 0)
                nameExpenseEt.setError("Wpisz nazwe wydatku!");
            else if (costD == 0)
                costExpenseEt.setError("Wpisz poprawną kwote wydatku !");
            else if (selectedUsersLogin.size() == 0)
                Toast.makeText(EditExpenseActivity.this, "Wybierz osoby dla których zrobiony jest wydatek", Toast.LENGTH_LONG).show();
            else if (imageBitmap != null) {
                expenseService.uploadReceiptImage(imageBitmap, accessToken, nameExpenseEt.getText().toString(), path -> {
                    Expense editExpense = new Expense(nameExpenseEt.getText().toString(), path, costD, selectedCategory, expenseOwner);
                    ExpenseHolder editExpenseHolder = new ExpenseHolder(editExpense, selectedUsersLogin);
                    expenseService.editExpenseById(accessToken, expenseId, editExpenseHolder, walletId);
                });
            } else {
                Expense editExpense = new Expense(nameExpenseEt.getText().toString(), imagePath, costD, selectedCategory, expenseOwner);
                ExpenseHolder editExpenseHolder = new ExpenseHolder(editExpense, selectedUsersLogin);
                expenseService.editExpenseById(accessToken, expenseId, editExpenseHolder, walletId);
            }
        });
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
                    receiptIv.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else
                Toast.makeText(this, "Wybierz zdjęcie o mniejszej rozdzielczości", Toast.LENGTH_SHORT).show();

            receiptIv.setVisibility(View.VISIBLE);
            deletePhotoBtn.setVisibility(View.VISIBLE);
        }
    }

    public double validateCost(String cost) {
        if (Pattern.compile("^\\d{0,8}(\\.\\d{1,2})?$").matcher(cost).matches()) {
            try {
                return Double.parseDouble(cost);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else return 0;
    }
}