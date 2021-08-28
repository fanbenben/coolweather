package com.example.coolweather.util;

import static org.litepal.LitePalApplication.getContext;

import com.example.coolweather.db.DatabaseHelper;

public class SingleDBHelper {
    private static DatabaseHelper dbHelper;
    public static DatabaseHelper getDatabaseHelper() {
        if (dbHelper == null) {
            synchronized (SingleDBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DatabaseHelper(getContext(), "Place.db", null, 1);
                }
            }
        }
        return dbHelper;
    }
}
