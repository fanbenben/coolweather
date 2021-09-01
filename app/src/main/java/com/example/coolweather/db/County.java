package com.example.coolweather.db;

public class County {
    private Integer code;
    private String name;
    private Integer cityCode;
    private Integer provinceCode;

    public County() {
    }

    public County(Integer code, String name, Integer cityCode, Integer provinceCode) {
        this.code = code;
        this.name = name;
        this.cityCode = cityCode;
        this.provinceCode = provinceCode;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
