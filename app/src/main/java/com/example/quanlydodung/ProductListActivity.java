package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.quanlydodung.adapters.DoDungAdapter;
import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.DoDung;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinnerSort;
    ImageButton btnAddProduct;
    RecyclerView recyclerProducts;
    LinearLayout emptyState;

    DBHelper db;
    ArrayList<DoDung> productList;
    DoDungAdapter adapter;

    int categoryId = -1;
    String categoryName = "Tất cả sản phẩm";

    private static final int REQUEST_ADD_PRODUCT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        db = new DBHelper(this);

        // Get category from intent
        categoryId = getIntent().getIntExtra("loaiId", -1);
        if (categoryId == -1) {
            categoryId = getIntent().getIntExtra("categoryId", -1);
        }

        categoryName = getIntent().getStringExtra("tenLoai");
        if (categoryName == null) {
            categoryName = getIntent().getStringExtra("categoryName");
        }
        if (categoryName == null) {
            categoryName = "Tất cả sản phẩm";
        }

        initViews();
        setupToolbar();
        setupSortSpinner();
        loadProducts();

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddDoDungActivity.class);
            if (categoryId != -1) {
                intent.putExtra("categoryId", categoryId);
            }
            startActivityForResult(intent, REQUEST_ADD_PRODUCT);
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        spinnerSort = findViewById(R.id.spinnerSort);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        emptyState = findViewById(R.id.emptyState);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        // Không gọi setSupportActionBar() để tránh IllegalStateException
        toolbar.setTitle(categoryName);
        // Thiết lập nút back
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupSortSpinner() {
        String[] sortOptions = {
                "Mới nhất",
                "Giá tăng dần",
                "Giá giảm dần",
                "Tên A-Z"
        };

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortProducts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadProducts() {
        if (categoryId == -1) {
            productList = db.getAllDoDung();
        } else {
            productList = db.getDoDungByLoai(categoryId);
        }

        updateUI();
    }

    private void sortProducts(int sortType) {
        if (productList == null || productList.isEmpty()) return;

        switch (sortType) {
            case 0: // Mới nhất (default - reload from database)
                loadProducts();
                return; // Return early, loadProducts already calls updateUI

            case 1: // Giá tăng dần
                productList.sort((p1, p2) -> Double.compare(p1.getGia(), p2.getGia()));
                break;

            case 2: // Giá giảm dần
                productList.sort((p1, p2) -> Double.compare(p2.getGia(), p1.getGia()));
                break;

            case 3: // Tên A-Z
                productList.sort((p1, p2) -> p1.getTenDoDung().compareTo(p2.getTenDoDung()));
                break;
        }

        // Update adapter with sorted list
        if (adapter != null) {
            adapter.updateList(productList);
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        if (productList == null || productList.isEmpty()) {
            recyclerProducts.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerProducts.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new DoDungAdapter(this, productList);
                recyclerProducts.setAdapter(adapter);
            } else {
                adapter.updateList(productList);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_PRODUCT && resultCode == RESULT_OK) {
            loadProducts();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
