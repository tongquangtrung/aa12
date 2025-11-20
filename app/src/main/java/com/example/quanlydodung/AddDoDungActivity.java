package com.example.quanlydodung;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.LoaiDoDung;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddDoDungActivity extends AppCompatActivity {

    EditText edtTen, edtGia;
    Spinner spinnerLoai;
    ImageView imgChonAnh;
    Button btnThem;

    DBHelper dbHelper;
    ArrayList<LoaiDoDung> listLoai = new ArrayList<>();
    String encodedImage = "";   // ảnh base64

    private static final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_do_dung);

        dbHelper = new DBHelper(this);

        initViews();
        loadLoaiDoDung();

        imgChonAnh.setOnClickListener(v -> chooseImage());

        btnThem.setOnClickListener(v -> addDoDung());
    }

    private void initViews() {
        edtTen = findViewById(R.id.edtTenDoDung);
        edtGia = findViewById(R.id.edtGia);
        spinnerLoai = findViewById(R.id.spinnerLoai);
        imgChonAnh = findViewById(R.id.imgChonAnh);
        btnThem = findViewById(R.id.btnThemDoDung);
    }

    // =====================================================
    //                   LOAD LOẠI ĐỒ DÙNG
    // =====================================================
    private void loadLoaiDoDung() {
        listLoai = dbHelper.getAllLoai();

        ArrayList<String> tenLoaiList = new ArrayList<>();
        for (LoaiDoDung l : listLoai) {
            tenLoaiList.add(l.getTenLoai());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tenLoaiList
        );
        spinnerLoai.setAdapter(adapter);
    }

    // =====================================================
    //                   CHỌN ẢNH
    // =====================================================
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgChonAnh.setImageURI(uri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Resize ảnh để tránh OutOfMemoryError
                Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800);
                if (bitmap != resizedBitmap) {
                    bitmap.recycle(); // Giải phóng bitmap gốc
                }

                encodedImage = convertToBase64(resizedBitmap);
                resizedBitmap.recycle(); // Giải phóng bitmap sau khi encode
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tải ảnh!", Toast.LENGTH_SHORT).show();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast.makeText(this, "Ảnh quá lớn, vui lòng chọn ảnh khác!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap;
        }

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
    }

    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); // Giảm xuống 50% chất lượng
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    // =====================================================
    //                   THÊM ĐỒ DÙNG
    // =====================================================
    private void addDoDung() {

        String ten = edtTen.getText().toString().trim();
        String giaStr = edtGia.getText().toString().trim();

        if (ten.isEmpty() || giaStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listLoai.size() == 0) {
            Toast.makeText(this, "Chưa có loại đồ dùng, hãy thêm trước!", Toast.LENGTH_SHORT).show();
            return;
        }

        int loaiId = listLoai.get(spinnerLoai.getSelectedItemPosition()).getId();
        double gia = Double.parseDouble(giaStr);

        long result = dbHelper.insertDoDung(ten, loaiId, gia, encodedImage);

        if (result > 0) {
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            finish(); // quay lại MainActivity
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
