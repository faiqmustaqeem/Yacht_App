package com.digitalexperts.bookyachts.models;

/**
 * Created by Irtiza on 9/17/2017.
 */

public class SubFacilityModel {
    private String id;
    private String facilityId;
    private String subFacilityName;
    private String subFacilityPrice;
    private String name;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected ;


    public String getSubFacilityName()
    {
        return subFacilityName;
    }

    public void setSubFacilityName(String subFacilityName)
    {
        this.subFacilityName = subFacilityName;
    }

    public String getFacilityId()
    {
        return facilityId;
    }

    public void setFacilityId(String facilityId)
    {
        this.facilityId = facilityId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSubFacilityPrice()
    {
        return subFacilityPrice;
    }

    public void setSubFacilityPrice(String subFacilityPrice)
    {
        this.subFacilityPrice = subFacilityPrice;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
