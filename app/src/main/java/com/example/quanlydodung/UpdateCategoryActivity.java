package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.LoaiDoDung;

public class UpdateCategoryActivity extends AppCompatActivity {

    EditText edtTenLoai, edtMoTa;
    TextView tvSelectedIcon;
    Button btnUpdate, btnCancel;
    DBHelper db;
    String selectedIcon = "📦";
    int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Initialize views
        edtTenLoai = findViewById(R.id.edtTenLoai);
        edtMoTa = findViewById(R.id.edtMoTa);
        tvSelectedIcon = findViewById(R.id.tvSelectedIcon);
        btnUpdate = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnUpdate.setText("Cập nhật");

        db = new DBHelper(this);

        // Get category ID from intent
        categoryId = getIntent().getIntExtra("categoryId", -1);
        if (categoryId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load category data
        loadCategoryData();

        // Setup icon selection
        setupIconSelection();

        // Button listeners
        btnUpdate.setOnClickListener(v -> updateCategory());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadCategoryData() {
        LoaiDoDung loai = db.getLoaiById(categoryId);
        if (loai != null) {
            edtTenLoai.setText(loai.getTenLoai());
            edtMoTa.setText(loai.getMoTa());
            selectedIcon = loai.getIcon() != null ? loai.getIcon() : "📦";
            tvSelectedIcon.setText(selectedIcon);
        } else {
            Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            finish();
        }
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

    private void updateCategory() {
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

        // Update in database
        boolean success = db.updateLoaiDoDung(categoryId, tenLoai, moTa, selectedIcon);

        if (success) {
            Toast.makeText(this, "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật danh mục", Toast.LENGTH_SHORT).show();
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

