package com.digitalexperts.bookyachts.models;

/**
 * Created by CodianSoft on 13/12/2017.
 */

public class CountryModel {
    private String countryName;
    private String countryId;

    public CountryModel(String countryName , String countryId)
    {
        this.setCountryName(countryName);
        this.setCountryId(countryId);
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
