package com.example.coolweather.db;

public class Cities {
    private Integer areacode;
    private Integer province_geocode;
    private String province;
    private Integer city_geocode;
    private String city;
    private Integer district_geocode;
    private String district;

    public Integer getAreacode() {
        return areacode;
    }

    public void setAreacode(Integer areacode) {
        this.areacode = areacode;
    }

    public Integer getProvince_geocode() {
        return province_geocode;
    }

    public void setProvince_geocode(Integer province_geocode) {
        this.province_geocode = province_geocode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCity_geocode() {
        return city_geocode;
    }

    public void setCity_geocode(Integer city_geocode) {
        this.city_geocode = city_geocode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDistrict_geocode() {
        return district_geocode;
    }

    public void setDistrict_geocode(Integer district_geocode) {
        this.district_geocode = district_geocode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
