package com.example.lab5.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SanPhamSQLite extends SQLiteOpenHelper {
    public static String DB_NAME = "QLSANPHAM";
    public  static int DB_VERSION = 1;
    public SanPhamSQLite(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE SANPHAM(id integer primary key autoincrement, tensp TEXT, giasp integer, hinhsp TEXT)";
        db.execSQL(sql);

        String sqlInsert = "INSERT INTO SANPHAM VALUES(1,'sản phẩm A',1000,'img.png')," +
                "(2,'sản phẩm B',3000,'img.png')," +
                "(3,'sản phẩm C',6000,'img.png')";
        db.execSQL((sqlInsert));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS SANPHAM";
        db.execSQL(sql);
        onCreate(db);
    }
}
