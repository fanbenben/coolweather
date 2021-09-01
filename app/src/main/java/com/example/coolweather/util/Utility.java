package com.example.coolweather.util;

import com.example.coolweather.entity.Forecast;
import com.example.coolweather.entity.Lives;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Utility {
    /**
     * 将返回的JSON数据解析成Forecast实体类
     */
    public static Forecast handleForecastResponse(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<Forecast>() {
        }.getType();
        return gson.fromJson(response, type);
    }
    /**
     * 将返回的JSON数据解析成Lives实体类
     */
    public static Lives handleLivesResponse(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<Lives>() {
        }.getType();
        return gson.fromJson(response, type);
    }

}
