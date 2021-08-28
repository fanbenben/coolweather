package com.example.coolweather;

import static org.litepal.LitePalApplication.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        initData();
    }

    private void initData() {
        dbHelper = SingleDBHelper.getDatabaseHelper();
        context = getContext();
        db = dbHelper.getWritableDatabase();
        addProvinceData();
        addCityData();
        addCountyData();
    }

    /**
     * 往数据库添加省的数据
     *
     * @param
     */
    public void addProvinceData() {
        try {
            InputStream in = context.getAssets().open("provinces.json");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Province>>() {
            }.getType();
            List<Province> provinceList = gson.fromJson(getString(in), type);
            Cursor cursor = db.query("Province", null, "name=? and code=?",
                    new String[]{provinceList.get(0).getName(), provinceList.get(0).getCode()}, null, null, null);
            if (cursor.getCount() > 0) return;
            ContentValues values = new ContentValues();
            for (Province province : provinceList) {
                values.put("code", province.getCode());
                values.put("name", province.getName());
                db.insert("Province", null, values);
                values.clear();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 往数据库添加市的数据
     *
     * @param
     */
    public void addCityData() {
        try {
            InputStream in = context.getAssets().open("cities.json");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<City>>() {
            }.getType();
            List<City> cityList = gson.fromJson(getString(in), type);
            Cursor cursor = db.query("City", null, "name=? and code=?",
                    new String[]{cityList.get(0).getName(), cityList.get(0).getCode()}, null, null, null);
            if (cursor.getCount() > 0) return;
            ContentValues values = new ContentValues();
            for (City city : cityList) {
                values.put("provinceCode", city.getProvinceCode());
                values.put("code", city.getCode());
                values.put("name", city.getName());
                db.insert("City", null, values);
                values.clear();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 往数据库添加县的数据
     *
     * @param
     */
    public void addCountyData() {
        try {
            InputStream in = context.getAssets().open("areas.json");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<County>>() {
            }.getType();
            List<County> countyList = gson.fromJson(getString(in), type);
            Cursor cursor = db.query("County", null, "name=? and code=?",
                    new String[]{countyList.get(0).getName(), countyList.get(0).getCode()}, null, null, null);
            if (cursor.getCount() > 0) return;
            ContentValues values = new ContentValues();
            for (County county : countyList) {
                values.put("code", county.getCode());
                values.put("name", county.getName());
                values.put("cityCode", county.getCityCode());
                values.put("provinceCode", county.getProvinceCode());
                db.insert("County", null, values);
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