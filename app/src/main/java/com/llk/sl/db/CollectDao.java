package com.llk.sl.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CollectDao {
    public static CollectDao getInstance() {
        return InnerHolder.mInstance;
    }

    private static class InnerHolder {
        private static CollectDao mInstance = new CollectDao();
    }

    private LocationDB helper;

    public void initDB(Context context) {
        this.helper = new LocationDB(context, "location", 1);
    }

    public void insert(String note, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.execSQL("insert into location_table(note,address,latitude,longitude) values(?,?,?,?)", new String[]{note, address, String.valueOf(latitude), String.valueOf(longitude)});
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.execSQL("delete from location_table where id=" + id);
    }

    public List<CollectModel> findAll() {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ArrayList<CollectModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from location_table", (String[])null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
            String longitude = cursor.getString(cursor.getColumnIndex("longitude"));

            double var18 = Double.parseDouble(longitude);
            double var16 = Double.parseDouble(latitude);
            CollectModel userInfo = new CollectModel(id, note, address, var18, var16);
            list.add(userInfo);
        }

        cursor.close();
        db.close();
        return list;
    }
}
