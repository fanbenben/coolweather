package com.example.coolweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coolweather.db.Cities;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.DatabaseHelper;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.SingleDBHelper;

import java.util.ArrayList;
import java.util.List;

/*这里我们需要编写用于编历省市县数据的碎片*/
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    /**
     * 省列表
     */
    private List<Province> provinceList = new ArrayList<>();
    /**
     * 市列表
     */
    private List<City> cityList = new ArrayList<>();
    /**
     * 县列表
     */
    private List<County> countyList = new ArrayList<>();
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
    private SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper dbHelper = SingleDBHelper.getDatabaseHelper();
        db = dbHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    County county = countyList.get(position);
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("WeatherCity", county.getCode());
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
                        weatherActivity.drawerlayout.closeDrawers();
                        weatherActivity.swipeRefresh.setRefreshing(true);
                        weatherActivity.requestWeatherAllInfo(String.valueOf(county.getCode()));
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    countyList.clear();
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    cityList.clear();
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到的再去服务器上查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        Cursor cursors = db.query(true, "Cities", new String[]{"province_geocode", "province"},
                null, null, null, null, null, null);
        dataList.clear();
        if (cursors.moveToFirst()) {
            do {
                int province_geocode = cursors.getInt(cursors.getColumnIndex("province_geocode"));
                String province1 = cursors.getString(cursors.getColumnIndex("province"));
                provinceList.add(new Province(province_geocode, province1));
                dataList.add(province1);
            } while (cursors.moveToNext());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_PROVINCE;
    }

    /**
     * 查询选中省份的所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getName());
        backButton.setVisibility(View.VISIBLE);
        Cursor cursors = db.query(true, "Cities", new String[]{"city_geocode", "city", "province_geocode"},
                "province_geocode=?", new String[]{String.valueOf(selectedProvince.getCode())},
                null, null, null, null);
        dataList.clear();
        if (cursors.moveToFirst()) {
            do {
                int code = cursors.getInt(cursors.getColumnIndex("city_geocode"));
                String name = cursors.getString(cursors.getColumnIndex("city"));
                int provinceCode = cursors.getInt(cursors.getColumnIndex("province_geocode"));
                cityList.add(new City(code, name, provinceCode));
                dataList.add(name);
            } while (cursors.moveToNext());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_CITY;
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getName());
        backButton.setVisibility(View.VISIBLE);
        Cursor cursors = db.query(true, "Cities", new String[]{"district_geocode", "district", "city_geocode", "province_geocode"}, "province_geocode=? and city_geocode=?",
                new String[]{String.valueOf(selectedProvince.getCode()), String.valueOf(selectedCity.getCode())},
                null, null, null, null);
        dataList.clear();
        if (cursors.moveToFirst()) {
            do {
                int code = cursors.getInt(cursors.getColumnIndex("district_geocode"));
                String name = cursors.getString(cursors.getColumnIndex("district"));
                int provinceCode = cursors.getInt(cursors.getColumnIndex("province_geocode"));
                int cityCode = cursors.getInt(cursors.getColumnIndex("city_geocode"));
                countyList.add(new County(code, name, cityCode, provinceCode));
                dataList.add(name);
            } while (cursors.moveToNext());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_COUNTY;
    }
}
