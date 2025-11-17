package com.example.quanlydodunghoctap.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class DoDung {
    private int id;
    private String tenDoDung;
    private int loaiDoDung;
    private double gia;
    private String anh; // base64

    public DoDung() {}

    public DoDung(int id, String tenDoDung, int loaiDoDung, double gia, String anh) {
        this.id = id;
        this.tenDoDung = tenDoDung;
        this.loaiDoDung = loaiDoDung;
        this.gia = gia;
        this.anh = anh;
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

    public Bitmap getBitmapImage() {
        if (anh == null || anh.isEmpty()) return null;
        try {
            byte[] decoded = Base64.decode(anh, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            return null;
        }
    }
}
