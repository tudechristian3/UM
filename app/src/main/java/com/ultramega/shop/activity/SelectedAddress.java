package com.ultramega.shop.activity;

public class SelectedAddress {
    private String DetailedAddress;
    private String Latitude;
    private String Longitude;

    public SelectedAddress(){
        super();
    }

    public SelectedAddress(String detailedAddress, String latitude, String longitude) {
        DetailedAddress = detailedAddress;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getDetailedAddress() {
        return DetailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        DetailedAddress = detailedAddress;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
