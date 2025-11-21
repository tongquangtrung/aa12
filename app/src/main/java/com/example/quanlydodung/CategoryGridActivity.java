package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.LoaiDoDung;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryGridActivity extends AppCompatActivity {

    GridView gridCategories;
    FloatingActionButton fabAddCategory;
    DBHelper db;
    ArrayList<LoaiDoDung> categories;
    CategoryAdapter adapter;
    private static final int REQUEST_ADD_CATEGORY = 100;
    private static final int REQUEST_UPDATE_CATEGORY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_grid);

        try {
            db = new DBHelper(this);
            gridCategories = findViewById(R.id.gridCategories);
            fabAddCategory = findViewById(R.id.fabAddCategory);

            // Load categories from database
            loadCategories();

            // Handle FAB click - Add new category
            fabAddCategory.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryGridActivity.this, AddCategoryActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CATEGORY);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ADD_CATEGORY || requestCode == REQUEST_UPDATE_CATEGORY)
                && resultCode == RESULT_OK) {
            // Reload categories after adding/updating
            loadCategories();
        }
    }

    private void loadCategories() {
        categories = db.getAllLoai();
        if (adapter == null) {
            adapter = new CategoryAdapter();
            gridCategories.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // Custom adapter for GridView
    class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_category_grid, parent, false);
            }

            LoaiDoDung loai = categories.get(position);

            TextView tvIcon = view.findViewById(R.id.tvCategoryIcon);
            TextView tvName = view.findViewById(R.id.tvCategoryName);
            TextView tvDesc = view.findViewById(R.id.tvCategoryDesc);
            ImageButton btnEdit = view.findViewById(R.id.btnEditCategory);
            ImageButton btnDelete = view.findViewById(R.id.btnDeleteCategory);
            View layoutContent = view.findViewById(R.id.layoutContent);

            tvIcon.setText(loai.getIcon() != null ? loai.getIcon() : "📦");
            tvName.setText(loai.getTenLoai());
            tvDesc.setText(loai.getMoTa());

            // Click on content to navigate to product list
            layoutContent.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryGridActivity.this, ProductListActivity.class);
                intent.putExtra("loaiId", loai.getId());
                intent.putExtra("tenLoai", loai.getTenLoai());
                startActivity(intent);
            });

            // Handle edit button
            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryGridActivity.this, UpdateCategoryActivity.class);
                intent.putExtra("categoryId", loai.getId());
                startActivityForResult(intent, REQUEST_UPDATE_CATEGORY);
            });

            // Handle delete button
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(CategoryGridActivity.this)
                        .setTitle("Xóa danh mục")
                        .setMessage("Bạn có chắc muốn xóa danh mục \"" + loai.getTenLoai() + "\"?\nTất cả sản phẩm trong danh mục này cũng sẽ bị xóa.")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            boolean success = db.deleteLoaiDoDung(loai.getId());
                            if (success) {
                                Toast.makeText(CategoryGridActivity.this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                                loadCategories();
                            } else {
                                Toast.makeText(CategoryGridActivity.this, "Lỗi khi xóa danh mục", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

            return view;
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
