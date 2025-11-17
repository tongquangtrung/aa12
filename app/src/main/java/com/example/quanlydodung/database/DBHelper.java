package com.example.quanlydodunghoctap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quanlydodunghoctap.models.DoDung;
import com.example.quanlydodunghoctap.models.LoaiDoDung;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuanLyDoDung.db";
    private static final int DB_VERSION = 1;

    // tables
    private static final String TABLE_LOAI = "LoaiDoDung";
    private static final String TABLE_DODUNG = "DoDung";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoai = "CREATE TABLE " + TABLE_LOAI + " (id INTEGER PRIMARY KEY AUTOINCREMENT, tenLoai TEXT, moTa TEXT)";
        String createDoDung = "CREATE TABLE " + TABLE_DODUNG + " (id INTEGER PRIMARY KEY AUTOINCREMENT, tenDoDung TEXT, loaiId INTEGER, gia REAL, anh TEXT, FOREIGN KEY(loaiId) REFERENCES " + TABLE_LOAI + "(id))";
        db.execSQL(createLoai);
        db.execSQL(createDoDung);

        // sample data
        db.execSQL("INSERT INTO " + TABLE_LOAI + "(tenLoai, moTa) VALUES ('Đồ dùng viết','Bút, bút chì'),('Đồ dùng học sinh','Vở, balo')");
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh) VALUES ('Bút bi','1',12000,'')");
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh) VALUES ('Vở kẻ ngang','2',15000,'')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DODUNG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAI);
        onCreate(db);
    }

    // LoaiDoDung CRUD
    public boolean addLoaiDoDung(String tenLoai, String moTa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenLoai", tenLoai);
        cv.put("moTa", moTa);
        long id = db.insert(TABLE_LOAI, null, cv);
        return id != -1;
    }

    public ArrayList<LoaiDoDung> getAllLoaiDoDung() {
        ArrayList<LoaiDoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_LOAI + " ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new LoaiDoDung(c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean updateLoaiDoDung(int id, String tenLoai, String moTa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenLoai", tenLoai);
        cv.put("moTa", moTa);
        int rows = db.update(TABLE_LOAI, cv, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteLoaiDoDung(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // delete related DoDung first
        db.delete(TABLE_DODUNG, "loaiId=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TABLE_LOAI, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // DoDung CRUD
    public boolean insertDoDung(String ten, int loaiId, double gia, String anhBase64) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenDoDung", ten);
        cv.put("loaiId", loaiId);
        cv.put("gia", gia);
        cv.put("anh", anhBase64);
        long id = db.insert(TABLE_DODUNG, null, cv);
        return id != -1;
    }

    public ArrayList<DoDung> getAllDoDung() {
        ArrayList<DoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public DoDung getDoDungById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " WHERE id=?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            DoDung d = new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4));
            c.close();
            return d;
        }
        c.close();
        return null;
    }

    public boolean updateDoDung(DoDung d) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenDoDung", d.getTenDoDung());
        cv.put("loaiId", d.getLoaiDoDung());
        cv.put("gia", d.getGia());
        cv.put("anh", d.getAnh());
        int rows = db.update(TABLE_DODUNG, cv, "id=?", new String[]{String.valueOf(d.getId())});
        return rows > 0;
    }

    public boolean deleteDoDung(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_DODUNG, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public ArrayList<DoDung> searchDoDung(String keyword) {
        ArrayList<DoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " WHERE tenDoDung LIKE ? ORDER BY id DESC", new String[]{"%"+keyword+"%"});
        if (c.moveToFirst()) {
            do {
                list.add(new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}
