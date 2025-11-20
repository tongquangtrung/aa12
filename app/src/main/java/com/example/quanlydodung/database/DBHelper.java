package com.example.quanlydodung.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quanlydodung.models.DoDung;
import com.example.quanlydodung.models.LoaiDoDung;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuanLyDoDung.db";
    private static final int DB_VERSION = 2;

    // tables
    private static final String TABLE_LOAI = "LoaiDoDung";
    private static final String TABLE_DODUNG = "DoDung";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoai = "CREATE TABLE " + TABLE_LOAI + " (id INTEGER PRIMARY KEY AUTOINCREMENT, tenLoai TEXT, moTa TEXT, icon TEXT)";
        String createDoDung = "CREATE TABLE " + TABLE_DODUNG + " (id INTEGER PRIMARY KEY AUTOINCREMENT, tenDoDung TEXT, loaiId INTEGER, gia REAL, anh TEXT, moTa TEXT, FOREIGN KEY(loaiId) REFERENCES " + TABLE_LOAI + "(id))";
        db.execSQL(createLoai);
        db.execSQL(createDoDung);

        // Insert mock categories with icons (emoji as placeholder)
        db.execSQL("INSERT INTO " + TABLE_LOAI + "(tenLoai, moTa, icon) VALUES " +
                "('Đồ dùng viết', 'Bút, bút chì, bút màu, bút gel', '✏️')," +
                "('Đồ dùng học sinh', 'Vở, sách, balo, hộp bút', '🎒')," +
                "('Văn phòng phẩm', 'Giấy, kẹp, ghim, băng keo', '📋')," +
                "('Dụng cụ vẽ', 'Màu vẽ, cọ, bảng vẽ', '🎨')," +
                "('Thiết bị văn phòng', 'Máy tính, bàn phím, chuột', '💻')," +
                "('Dụng cụ học tập', 'Thước, compa, eke', '📐')");

        // Insert mock products - Đồ dùng viết (loaiId = 1)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Bút bi Thiên Long TL-027', 1, 3000, '', 'Bút bi cao cấp, mực xanh')," +
                "('Bút chì 2B Stabilo', 1, 5000, '', 'Bút chì gỗ, ngòi 2B')," +
                "('Bút gel Pentel Energel', 1, 15000, '', 'Bút gel mực nước, viết mượt')," +
                "('Bút màu Thiên Long CP-C03', 1, 45000, '', 'Hộp 12 màu')," +
                "('Bút dạ quang Stabilo Boss', 1, 18000, '', 'Bút đánh dấu văn bản')");

        // Đồ dùng học sinh (loaiId = 2)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Vở kẻ ngang 200 trang', 2, 15000, '', 'Vở học sinh cao cấp')," +
                "('Balo học sinh Nike', 2, 350000, '', 'Balo chống nước, nhiều ngăn')," +
                "('Hộp bút Miniso', 2, 25000, '', 'Hộp bút vải, nhiều màu')," +
                "('Sổ tay bìa da A5', 2, 45000, '', 'Sổ tay cao cấp 120 trang')," +
                "('Balo Adidas Classic', 2, 280000, '', 'Balo thể thao chính hãng')");

        // Văn phòng phẩm (loaiId = 3)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Giấy A4 Double A 70gsm', 3, 85000, '', 'Ream 500 tờ')," +
                "('Kẹp giấy Deli', 3, 8000, '', 'Hộp 100 chiếc')," +
                "('Ghim bấm số 10', 3, 5000, '', 'Hộp 1000 chiếc')," +
                "('Băng keo trong 2 mặt', 3, 12000, '', 'Băng keo 2 mặt 5m')," +
                "('Bìa còng Plus A4', 3, 18000, '', 'Bìa đựng tài liệu')");

        // Dụng cụ vẽ (loaiId = 4)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Màu nước Thiên Long 12 màu', 4, 35000, '', 'Bộ màu nước học sinh')," +
                "('Cọ vẽ Artline 6 cây', 4, 50000, '', 'Bộ cọ nhiều cỡ')," +
                "('Bảng vẽ A3', 4, 25000, '', 'Bảng vẽ gỗ chuyên dụng')," +
                "('Sáp màu Crayola 24 màu', 4, 65000, '', 'Sáp màu cao cấp')," +
                "('Màu acrylic 12 tuýp', 4, 120000, '', 'Màu vẽ chuyên nghiệp')");

        // Thiết bị văn phòng (loaiId = 5)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Bàn phím cơ Logitech', 5, 850000, '', 'Bàn phím cơ RGB')," +
                "('Chuột không dây Logitech', 5, 250000, '', 'Chuột wireless DPI cao')," +
                "('Máy tính bỏ túi Casio', 5, 180000, '', 'Máy tính khoa học')," +
                "('USB Kingston 32GB', 5, 120000, '', 'USB 3.0 tốc độ cao')," +
                "('Tai nghe Bluetooth Sony', 5, 950000, '', 'Tai nghe chống ồn')");

        // Dụng cụ học tập (loaiId = 6)
        db.execSQL("INSERT INTO " + TABLE_DODUNG + "(tenDoDung, loaiId, gia, anh, moTa) VALUES " +
                "('Thước kẻ nhựa 30cm', 6, 8000, '', 'Thước trong suốt')," +
                "('Compa vẽ hình Kim Thành', 6, 35000, '', 'Compa kim loại cao cấp')," +
                "('Bộ eke 3 món', 6, 15000, '', 'Eke nhựa trong suốt')," +
                "('Thước đo góc 180 độ', 6, 12000, '', 'Thước đo góc chính xác')," +
                "('Bộ dụng cụ học tập 8 món', 6, 65000, '', 'Bộ dụng cụ đầy đủ')");
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
        cv.put("icon", "📦");
        long id = db.insert(TABLE_LOAI, null, cv);
        return id != -1;
    }

    public boolean addLoaiDoDungWithIcon(String tenLoai, String moTa, String icon) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenLoai", tenLoai);
        cv.put("moTa", moTa);
        cv.put("icon", icon);
        long id = db.insert(TABLE_LOAI, null, cv);
        return id != -1;
    }

    public ArrayList<LoaiDoDung> getAllLoai() {
        ArrayList<LoaiDoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_LOAI + " ORDER BY id ASC", null);
        if (c.moveToFirst()) {
            do {
                LoaiDoDung loai = new LoaiDoDung(c.getInt(0), c.getString(1), c.getString(2));
                if (c.getColumnCount() > 3) {
                    loai.setIcon(c.getString(3));
                }
                list.add(loai);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public ArrayList<LoaiDoDung> getAllLoaiDoDung() {
        return getAllLoai();
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
    public long insertDoDung(String ten, int loaiId, double gia, String anhBase64) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenDoDung", ten);
        cv.put("loaiId", loaiId);
        cv.put("gia", gia);
        cv.put("anh", anhBase64);
        cv.put("moTa", "");
        return db.insert(TABLE_DODUNG, null, cv);
    }

    public ArrayList<DoDung> getAllDoDung() {
        ArrayList<DoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " ORDER BY id DESC", null);
        if (c.moveToFirst()) {
            do {
                DoDung d = new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4));
                if (c.getColumnCount() > 5) {
                    d.setMoTa(c.getString(5));
                }
                list.add(d);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // NEW: Get products by category
    public ArrayList<DoDung> getDoDungByLoai(int loaiId) {
        ArrayList<DoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " WHERE loaiId=? ORDER BY id DESC", new String[]{String.valueOf(loaiId)});
        if (c.moveToFirst()) {
            do {
                DoDung d = new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4));
                if (c.getColumnCount() > 5) {
                    d.setMoTa(c.getString(5));
                }
                list.add(d);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // NEW: Get products by category with sorting
    public ArrayList<DoDung> getDoDungByLoaiSorted(int loaiId, String sortBy) {
        ArrayList<DoDung> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String orderBy = "id DESC";
        if ("price_asc".equals(sortBy)) {
            orderBy = "gia ASC";
        } else if ("price_desc".equals(sortBy)) {
            orderBy = "gia DESC";
        } else if ("name_asc".equals(sortBy)) {
            orderBy = "tenDoDung ASC";
        }

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " WHERE loaiId=? ORDER BY " + orderBy, new String[]{String.valueOf(loaiId)});
        if (c.moveToFirst()) {
            do {
                DoDung d = new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4));
                if (c.getColumnCount() > 5) {
                    d.setMoTa(c.getString(5));
                }
                list.add(d);
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
            if (c.getColumnCount() > 5) {
                d.setMoTa(c.getString(5));
            }
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
        if (d.getMoTa() != null) {
            cv.put("moTa", d.getMoTa());
        }
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
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DODUNG + " WHERE tenDoDung LIKE ? ORDER BY id DESC", new String[]{"%" + keyword + "%"});
        if (c.moveToFirst()) {
            do {
                DoDung d = new DoDung(c.getInt(0), c.getString(1), c.getInt(2), c.getDouble(3), c.getString(4));
                if (c.getColumnCount() > 5) {
                    d.setMoTa(c.getString(5));
                }
                list.add(d);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}
