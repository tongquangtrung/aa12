package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydodung.database.DBHelper;

public class AddCategoryActivity extends AppCompatActivity {

    EditText edtTenLoai, edtMoTa;
    TextView tvSelectedIcon;
    Button btnSave, btnCancel;
    DBHelper db;
    String selectedIcon = "📦";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Initialize views
        edtTenLoai = findViewById(R.id.edtTenLoai);
        edtMoTa = findViewById(R.id.edtMoTa);
        tvSelectedIcon = findViewById(R.id.tvSelectedIcon);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = new DBHelper(this);

        // Setup icon selection
        setupIconSelection();

        // Button listeners
        btnSave.setOnClickListener(v -> saveCategory());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupIconSelection() {
        int[] iconIds = {
            R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5,
            R.id.icon6, R.id.icon7, R.id.icon8, R.id.icon9, R.id.icon10
        };

        View.OnClickListener iconClickListener = v -> {
            TextView iconView = (TextView) v;
            selectedIcon = iconView.getTag().toString();
            tvSelectedIcon.setText(selectedIcon);

            // Visual feedback
            for (int id : iconIds) {
                findViewById(id).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
            v.setBackgroundColor(getResources().getColor(R.color.teal_200));
        };

        for (int id : iconIds) {
            findViewById(id).setOnClickListener(iconClickListener);
        }
    }

    private void saveCategory() {
        String tenLoai = edtTenLoai.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        // Validation
        if (tenLoai.isEmpty()) {
            edtTenLoai.setError("Vui lòng nhập tên danh mục");
            edtTenLoai.requestFocus();
            return;
        }

        if (moTa.isEmpty()) {
            moTa = "Chưa có mô tả";
        }

        // Save to database
        boolean success = db.addLoaiDoDungWithIcon(tenLoai, moTa, selectedIcon);

        if (success) {
            Toast.makeText(this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}

