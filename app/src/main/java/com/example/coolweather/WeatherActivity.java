package com.example.coolweather;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.coolweather.entity.Forecast;
import com.example.coolweather.entity.Lives;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private final String key = "ad607faec3d2df2825617b700a38c1a7";

    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerlayout;
    private TextView nowCity;
    private TextView nowCityUp;
    private Button navButton;
    private ProgressDialog progress;
    private ScrollView weatherLayout;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private String lives_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        drawerlayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        nowCity = findViewById(R.id.now_city);
        nowCityUp = findViewById(R.id.now_city_up);
        weatherLayout = findViewById(R.id.weather_layout);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefsString = prefs.getString("weather", null);
        String forecastString = prefs.getString("forecast", null);
        if (prefsString != null) {
            //有缓存时直接解析天气
            Lives lives = Utility.handleLivesResponse(prefsString);
            lives_item = lives.getLives().get(0).getAdcode();
            showLives(lives);
            Forecast forecast = Utility.handleForecastResponse(forecastString);
            showForecast(forecast);
        } else {
            //去服务器查询天气
            int code = getIntent().getIntExtra("WeatherCity", 0);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherAllInfo(String.valueOf(code));
        }
        String bing_pic = prefs.getString("bing_pic", null);
        if (bing_pic != null) {
            Glide.with(this).load(bing_pic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherAllInfo(lives_item);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //此处是请求各种天气信息
    public void requestWeatherAllInfo(String id) {
        lives_item = id;
        requestForecast(id);
        requestLives(id);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    public void requestForecast(String cityId) {
        showProgressDialog();
        String cityUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + cityId + "&key=" + key
                + "&extensions=all";
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Forecast forecast = Utility.handleForecastResponse(string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        if (forecast != null && "1".equals(forecast.getStatus())) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("forecast", string);
                            editor.apply();
                            showForecast(forecast);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取城市实时天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        //这个是表示事件刷新结束
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取城市实时天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void requestLives(String cityId) {
        String cityUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + cityId + "&key=" + key
                + "&extensions=base";
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Lives lives = Utility.handleLivesResponse(string);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lives != null && "1".equals(lives.getStatus())) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", string);
                            editor.apply();
                            showLives(lives);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取城市未来天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取城市未来天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showLives(Lives lives) {
        nowCity.setText(lives.getLives().get(0).getCity());
        nowCityUp.setText(lives.getLives().get(0).getProvince());
        titleUpdateTime.setText(lives.getLives().get(0).getReporttime());
        degreeText.setText(lives.getLives().get(0).getTemperature() + "℃");
        weatherInfoText.setText(lives.getLives().get(0).getWeather());
        sportText.setText("风向：" + lives.getLives().get(0).getWinddirection());
        carWashText.setText("风力等级：" + lives.getLives().get(0).getWindpower());
        aqiText.setText("空气湿度：" + lives.getLives().get(0).getHumidity());
        comfortText.setText("舒适指数：舒适");
        pm25Text.setText("污染指数：轻度污染");
    }

    private void showForecast(Forecast weather) {
        forecastLayout.removeAllViews();
        List<Forecast.ForecastsDTO.CastsDTO> casts = weather.getForecasts().get(0).getCasts();
        for (Forecast.ForecastsDTO.CastsDTO cast : casts) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView data_text = view.findViewById(R.id.data_text);
            TextView info_text = view.findViewById(R.id.info_text);
            TextView max_text = view.findViewById(R.id.max_text);
            TextView min_text = view.findViewById(R.id.min_text);
            int i = cast.getDate().indexOf("-");
            String substring = cast.getDate().substring(i + 1);
            data_text.setText(substring);
            info_text.setText(cast.getDayweather());
            max_text.setText(cast.getDaytemp() + "℃");
            min_text.setText(cast.getNighttemp() + "℃");
            forecastLayout.addView(view);
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progress == null) {
            progress = new ProgressDialog(WeatherActivity.this);
            progress.setMessage("客官请稍等，正在加载中");
            progress.setCanceledOnTouchOutside(false);
        }
        progress.show();
    }

    /**
     * 隐藏进度对话框
     */
    private void hideProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });

    }
}