package com.example.quanlydodunghoctap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.quanlydodunghoctap.database.DBHelper;
import com.example.quanlydodunghoctap.models.DoDung;

public class UpdateDoDungActivity extends AppCompatActivity {

    EditText edtTen, edtMoTa, edtLoai;
    Button btnSave;
    DBHelper db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_do_dung);

        db = new DBHelper(this);

        id = getIntent().getIntExtra("id", -1);
        DoDung d = db.getDoDungById(id);

        edtTen = findViewById(R.id.edtTenUpdate);
        edtMoTa = findViewById(R.id.edtMoTaUpdate);
        edtLoai = findViewById(R.id.edtLoaiUpdate);
        btnSave = findViewById(R.id.btnUpdate);

        edtTen.setText(d.getTen());
        edtMoTa.setText(d.getMoTa());
        edtLoai.setText(d.getLoai());

        btnSave.setOnClickListener(v -> {
            db.updateDoDung(id,
                    edtTen.getText().toString(),
                    edtMoTa.getText().toString(),
                    edtLoai.getText().toString());
            finish();
        });
    }
}
