package com.ultramega.shop.pojo;

/**
 * Created by User-PC on 6/5/2017.
 */
public class Address {
    private String name, mobileNumber, detailedAddress, barangay, city, region, provice, zip;

    public Address(){
        super();
    }

    public Address(String name, String mobileNumber, String detailedAddress, String barangay, String city, String region, String provice, String zip) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.detailedAddress = detailedAddress;
        this.barangay = barangay;
        this.city = city;
        this.region = region;
        this.provice = provice;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
