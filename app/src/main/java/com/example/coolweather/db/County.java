package com.example.coolweather.db;

public class County {
    private String code;
    private String name;
    private String cityCode;
    private String provinceCode;

    public County() {
    }

    public County(String code, String name, String cityCode, String provinceCode) {
        this.code = code;
        this.name = name;
        this.cityCode = cityCode;
        this.provinceCode = provinceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
