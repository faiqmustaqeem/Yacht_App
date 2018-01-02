package com.digitalexperts.bookyachts.models;

/**
 * Created by CodianSoft on 13/12/2017.
 */

public class CityModel {
    private String cityName;
    private String cityid;

    public CityModel(String cityName , String cityid)
    {
        setCityName(cityName);
        setCityid(cityid);
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
