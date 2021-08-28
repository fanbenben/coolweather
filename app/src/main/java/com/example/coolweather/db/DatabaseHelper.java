package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_PROVINCE = "create table Province(" +
            "code text," +
            "name text)";

    private static final String CREATE_City = "create table City(" +
            "code text," +
            "name text," +
            "provinceCode text)";

    private static final String CREATE_County = "create table County(" +
            "code text," +
            "name text," +
            "cityCode text," +
            "provinceCode text)";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_City);
        db.execSQL(CREATE_County);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
