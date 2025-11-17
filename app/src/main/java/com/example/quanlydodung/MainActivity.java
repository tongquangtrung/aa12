package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.quanlydodung.adapters.DoDungAdapter;
import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.DoDung;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerDoDung;
    ImageButton btnAdd;
    ArrayList<DoDung> list;
    DBHelper db;

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        recyclerDoDung = findViewById(R.id.recyclerDoDung);
        btnAdd = findViewById(R.id.btnAddDoDung);

        recyclerDoDung.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddDoDungActivity.class));
        });

        loadData();
    }

    void loadData() {
        list = db.getAllDoDung();
        DoDungAdapter adapter = new DoDungAdapter(this, list);
        recyclerDoDung.setAdapter(adapter);
    }
}
