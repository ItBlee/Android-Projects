package com.example.lab5.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab5.database.SanPhamSQLite;
import com.example.lab5.model.SanPham;

import java.util.ArrayList;

public class SanPhamDAO {
    private SanPhamSQLite sanPhamSQLite;
    public SanPhamDAO(Context context){
        sanPhamSQLite = new SanPhamSQLite(context);
    }

    public ArrayList<SanPham> getAll(){
        ArrayList<SanPham> list = new ArrayList<SanPham>();
        SQLiteDatabase database = sanPhamSQLite.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM SANPHAM", null);
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            do{
                list.add(new SanPham(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public boolean insertProduct(SanPham sp) {
        try {
            SQLiteDatabase database = sanPhamSQLite.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("tensp",sp.getTenSP());
            contentValues.put("giasp",sp.getGiaSP());
            contentValues.put("hinhsp",sp.getHinhSP());
            database.insert("SANPHAM", null, contentValues);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public  boolean updateProduct(SanPham sp){
        try {
            SQLiteDatabase database = sanPhamSQLite.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("tensp",sp.getTenSP());
            contentValues.put("giasp",sp.getGiaSP());
            contentValues.put("hinhsp",sp.getHinhSP());
            database.update("SANPHAM", contentValues, "id = ?", new String[]{String.valueOf(sp.getId())});

            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean deleteProduct(int idDelete) {
        return true;
    }
}
