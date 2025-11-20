package com.example.quanlydodung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.util.Base64;

import com.example.quanlydodung.database.DBHelper;
import com.example.quanlydodung.models.DoDung;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateDoDungActivity extends AppCompatActivity {

    EditText edtTen, edtGia;
    Spinner spinnerLoai;
    ImageView imgAnh;
    Button btnUpdate, btnChonAnh;
    DBHelper db;
    int id;
    String encodedImage = "";
    private static final int PICK_IMAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_do_dung);

        db = new DBHelper(this);

        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy đồ dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DoDung d = db.getDoDungById(id);
        if (d == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy đồ dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtTen = findViewById(R.id.edtTenDoDungUpdate);
        edtGia = findViewById(R.id.edtGiaUpdate);
        spinnerLoai = findViewById(R.id.spinnerLoaiUpdate);
        imgAnh = findViewById(R.id.imgAnhUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnChonAnh = findViewById(R.id.btnChonAnhUpdate);

        edtTen.setText(d.getTenDoDung());
        edtGia.setText(String.valueOf((int)d.getGia()));
        encodedImage = d.getAnh();

        if (d.getBitmapImage() != null) {
            imgAnh.setImageBitmap(d.getBitmapImage());
        }

        btnChonAnh.setOnClickListener(v -> chooseImage());

        btnUpdate.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String giaStr = edtGia.getText().toString().trim();

            if (ten.isEmpty() || giaStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            double gia = Double.parseDouble(giaStr);

            DoDung doDung = new DoDung(id, ten, d.getLoaiDoDung(), gia, encodedImage);
            boolean result = db.updateDoDung(doDung);

            if (result) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgAnh.setImageURI(uri);

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
}
