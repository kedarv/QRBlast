package com.example.kedar.qrblast;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data1121212.db";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table data (uid INTEGER PRIMARY KEY AUTOINCREMENT,segmentID integer,contents text,sessionID integer)"
        );
    }
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
}
