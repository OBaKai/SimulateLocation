package com.llk.sl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class LocationDB extends SQLiteOpenHelper {
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table location_table(id integer primary key autoincrement, note varchar(100), address varchar(100),latitude varchar(20),longitude varchar(20))");
    }

    public void onUpgrade(@Nullable SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public LocationDB(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, (SQLiteDatabase.CursorFactory)null, version);
    }
}
