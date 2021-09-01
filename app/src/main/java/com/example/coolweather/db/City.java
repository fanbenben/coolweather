package com.example.coolweather.db;

public class City {
    private Integer code;
    private String name;
    private Integer provinceCode;

    public City() {
    }

    public City(Integer code, String name, Integer provinceCode) {
        this.code = code;
        this.name = name;
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

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
