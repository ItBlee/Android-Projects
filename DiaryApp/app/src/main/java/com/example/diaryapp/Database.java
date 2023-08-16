package com.example.diaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteOpenHelper {
    public static final String databaseName = "diaryApp.db";
    public static final String tableName = "diaryTable";
    public static final String col_1 = "ID";
    public static final String col_2 = "title";
    public static final String col_3 = "content";
    public static final String col_4 = "dateTime";
    public static final String col_5 = "filePath";
    public static final String col_6 = "weather";
    public static final String col_7 = "location";

    public Database(Context context) {
        super(context, databaseName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tableName +
                " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "content TEXT," +
                    "dateTime Date," +
                    "filePath TEXT," +
                    "weather TEXT," +
                    "location TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + databaseName);
        onCreate(db);
    }

    public long insertDiary(Diary diary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, diary.getTitle());
        contentValues.put(col_3, diary.getContent());
        contentValues.put(col_4, diary.getDateTime());
        contentValues.put(col_5, diary.getFilePath());
        contentValues.put(col_6, diary.getWeather());
        contentValues.put(col_7, diary.getLocation());
        return db.insert(tableName, null, contentValues);
    }

    public Cursor displayDiary(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + tableName, null);
    }

    public boolean updateDiary(Diary diary){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(col_1, diary.getID());
            contentValues.put(col_2, diary.getTitle());
            contentValues.put(col_3, diary.getContent());
            contentValues.put(col_4, diary.getDateTime());
            contentValues.put(col_5, diary.getFilePath());
            contentValues.put(col_6, diary.getWeather());
            contentValues.put(col_7, diary.getLocation());
            db.update(tableName, contentValues,col_1 + " =?", new String[]{diary.getID()});
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean deleteDiary(String id){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.delete(tableName,col_1 + " = ?", new String[]{id});
        return true;
    }
}
