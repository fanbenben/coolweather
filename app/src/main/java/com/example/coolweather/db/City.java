package com.example.coolweather.db;

public class City {
    private String code;
    private String name;
    private String provinceCode;

    public City() {
    }

    public City(String code, String name, String provinceCode) {
        this.code = code;
        this.name = name;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
