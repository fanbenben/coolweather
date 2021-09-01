package com.example.coolweather;

import static org.litepal.LitePalApplication.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coolweather.db.Cities;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.DatabaseHelper;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.SingleDBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        initData();
    }

    private void initData() {
        dbHelper = SingleDBHelper.getDatabaseHelper();
        context = getContext();
        db = dbHelper.getWritableDatabase();
        addAllCitiesData();
    }

    /**
     * 往数据库添加省的数据
     *
     * @param
     */
    public void addAllCitiesData() {
        try {
            InputStream in = context.getAssets().open("allData.json");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Cities>>() {
            }.getType();
            List<Cities> citiesList = gson.fromJson(getString(in), type);
            //这里是避免重复添加数据
            Cursor cursor = db.query("Cities", null, "areacode=?",
                    new String[]{String.valueOf(citiesList.get(0).getAreacode())}, null, null, null);
            if (cursor.getCount() > 0) return;
            ContentValues values = new ContentValues();
            for (Cities cities : citiesList) {
                values.put("areacode", cities.getAreacode());
                values.put("province_geocode", cities.getProvince_geocode());
                values.put("province", cities.getProvince());
                values.put("city_geocode", cities.getCity_geocode());
                values.put("city", cities.getCity());
                values.put("district_geocode", cities.getDistrict_geocode());
                values.put("district", cities.getDistrict());
                db.insert("Cities", null, values);
                values.clear();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String getString(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}