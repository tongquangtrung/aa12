package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.quanlydodung.adapters.DoDungAdapter;
import com.example.quanlydodung.database.DBHelper;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearch;
    RecyclerView recyclerView;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DBHelper(this);

        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.lvSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();
                DoDungAdapter adapter = new DoDungAdapter(SearchActivity.this, db.searchDoDung(keyword));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
