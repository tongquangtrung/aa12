package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanlydodung.adapters.DoDungAdapter;
import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.DoDung;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerDoDung;
    ImageButton btnAdd;
    ArrayList<DoDung> list;
    DBHelper db;
    int categoryId = -1; // -1 means show all products
    String categoryName = "";

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = new DBHelper(this);

            // Get category info from intent if available
            categoryId = getIntent().getIntExtra("loaiId", -1);
            if (categoryId == -1) {
                categoryId = getIntent().getIntExtra("categoryId", -1);
            }
            categoryName = getIntent().getStringExtra("tenLoai");
            if (categoryName == null || categoryName.isEmpty()) {
                categoryName = getIntent().getStringExtra("categoryName");
            }

            // Set title based on category
            if (categoryId != -1 && categoryName != null && !categoryName.isEmpty()) {
                setTitle(categoryName);
            } else {
                setTitle("Tất cả sản phẩm");
            }

            recyclerDoDung = findViewById(R.id.recyclerDoDung);
            btnAdd = findViewById(R.id.btnAddDoDung);

            recyclerDoDung.setLayoutManager(new LinearLayoutManager(this));

            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddDoDungActivity.class);
                if (categoryId != -1) {
                    intent.putExtra("loaiId", categoryId);
                    intent.putExtra("categoryId", categoryId);
                }
                startActivity(intent);
            });

            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void loadData() {
        try {
            // Load products based on category
            if (categoryId != -1) {
                list = db.getDoDungByLoai(categoryId);
                if (list.isEmpty()) {
                    Toast.makeText(this, "Danh mục này chưa có sản phẩm", Toast.LENGTH_SHORT).show();
                }
            } else {
                list = db.getAllDoDung();
            }

            if (list == null) {
                list = new ArrayList<>();
            }
            DoDungAdapter adapter = new DoDungAdapter(this, list);
            recyclerDoDung.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
