package com.example.quanlydodunghoctap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quanlydodunghoctap.adapters.LoaiDoDungAdapter;
import com.example.quanlydodunghoctap.database.DBHelper;

public class LoaiDoDungActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_do_dung);

        db = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerLoaiDoDung);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LoaiDoDungAdapter adapter =
                new LoaiDoDungAdapter(this, db.getAllLoai());
        recyclerView.setAdapter(adapter);
    }
}
