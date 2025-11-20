package com.example.quanlydodung.models;

public class LoaiDoDung {
    private int id;
    private String tenLoai;
    private String moTa;
    private String icon;

    public LoaiDoDung() {}

    public LoaiDoDung(int id, String tenLoai, String moTa) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.icon = "📦";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
