package com.example.quanlydodunghoctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.quanlydodunghoctap.database.DBHelper;

public class AddDoDungActivity extends AppCompatActivity {

    EditText edtTen, edtMoTa, edtLoai;
    Button btnSave;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_do_dung);

        db = new DBHelper(this);

        edtTen = findViewById(R.id.edtTen);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtLoai = findViewById(R.id.edtLoai);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            db.addDoDung(
                    edtTen.getText().toString(),
                    edtMoTa.getText().toString(),
                    edtLoai.getText().toString()
            );
            finish();
        });
    }
}
