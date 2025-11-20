package com.example.quanlydodung.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class DoDung {
    private int id;
    private String tenDoDung;
    private int loaiDoDung;
    private double gia;
    private String anh; // base64
    private String moTa;

    public DoDung() {}

    public DoDung(int id, String tenDoDung, int loaiDoDung, double gia, String anh) {
        this.id = id;
        this.tenDoDung = tenDoDung;
        this.loaiDoDung = loaiDoDung;
        this.gia = gia;
        this.anh = anh;
        this.moTa = "";
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenDoDung() { return tenDoDung; }
    public void setTenDoDung(String tenDoDung) { this.tenDoDung = tenDoDung; }
    public int getLoaiDoDung() { return loaiDoDung; }
    public void setLoaiDoDung(int loaiDoDung) { this.loaiDoDung = loaiDoDung; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public String getAnh() { return anh; }
    public void setAnh(String anh) { this.anh = anh; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Bitmap getBitmapImage() {
        if (anh == null || anh.isEmpty()) return null;
        try {
            byte[] decoded = Base64.decode(anh, Base64.DEFAULT);

            // Decode với options để tránh OutOfMemoryError
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // Giảm kích thước ảnh xuống 1/2
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565; // Dùng ít bộ nhớ hơn

            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc(); // Gợi ý garbage collector dọn dẹp
            return null;
        }
    }
}
