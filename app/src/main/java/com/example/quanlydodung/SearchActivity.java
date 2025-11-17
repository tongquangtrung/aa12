package com.example.quanlydodunghoctap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.quanlydodunghoctap.adapters.DoDungAdapter;
import com.example.quanlydodunghoctap.database.DBHelper;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnSearch;
    RecyclerView recyclerView;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DBHelper(this);

        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString();
            DoDungAdapter adapter =
                    new DoDungAdapter(this, db.searchDoDung(keyword));
            recyclerView.setAdapter(adapter);
        });
    }
}
