package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 8/17/2017.
 */

public class Branches implements Parcelable {
    @SerializedName("NetworkID")
    @Expose
    private String NetworkID;
    @SerializedName("BranchID")
    @Expose
    private String BranchID;
    @SerializedName("BranchName")
    @Expose
    private String BranchName;
    @SerializedName("BranchType")
    @Expose
    private String BranchType;
    @SerializedName("BranchGroup")
    @Expose
    private String BranchGroup;
    @SerializedName("AuthorisedRepresentative")
    @Expose
    private String AuthorisedRepresentative;
    @SerializedName("StreetAddress")
    @Expose
    private String StreetAddress;
    @SerializedName("City")
    @Expose
    private String City;
    @SerializedName("Province")
    @Expose
    private String Province;
    @SerializedName("Zip")
    @Expose
    private int Zip;
    @SerializedName("Country")
    @Expose
    private String Country;
    @SerializedName("Longitude")
    @Expose
    private String Longitude;
    @SerializedName("Latitude")
    @Expose
    private String Latitude;
    @SerializedName("AuthorisedEmailAddress")
    @Expose
    private String AuthorisedEmailAddress;
    @SerializedName("AuthorisedTelNo")
    @Expose
    private String AuthorisedTelNo;
    @SerializedName("AuthorisedCountryCode")
    @Expose
    private String AuthorisedCountryCode;
    @SerializedName("AuthorisedMobileNo")
    @Expose
    private String AuthorisedMobileNo;
    @SerializedName("Fax")
    @Expose
    private String Fax;
    @SerializedName("CurrentOrderCount")
    @Expose
    private int CurrentOrderCount;
    @SerializedName("DateTimeAdded")
    @Expose
    private String DateTimeAdded;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("CalculatedDistance")
    @Expose
    private double CalculatedDistance;

    @SerializedName("StoreHoursStart")
    @Expose
    private String StoreHoursStart;


    @SerializedName("StoreHoursEnd")
    @Expose
    private String StoreHoursEnd;



    public Branches(String networkID, String branchID, String branchName, String branchType, String branchGroup, String authorisedRepresentative, String streetAddress, String city, String province, int zip, String country, String longitude, String latitude, String authorisedEmailAddress, String authorisedTelNo, String authorisedCountryCode, String authorisedMobileNo, String fax, int currentOrderCount, String dateTimeAdded, String status, double calculatedDistance,String storeOpen,String storeClose) {
        NetworkID = networkID;
        BranchID = branchID;
        BranchName = branchName;
        BranchType = branchType;
        BranchGroup = branchGroup;
        AuthorisedRepresentative = authorisedRepresentative;
        StreetAddress = streetAddress;
        City = city;
        Province = province;
        Zip = zip;
        Country = country;
        Longitude = longitude;
        Latitude = latitude;
        AuthorisedEmailAddress = authorisedEmailAddress;
        AuthorisedTelNo = authorisedTelNo;
        AuthorisedCountryCode = authorisedCountryCode;
        AuthorisedMobileNo = authorisedMobileNo;
        Fax = fax;
        CurrentOrderCount = currentOrderCount;
        DateTimeAdded = dateTimeAdded;
        Status = status;
        CalculatedDistance = calculatedDistance;
        StoreHoursStart = storeOpen;
        StoreHoursEnd = storeClose;
    }

    protected Branches(Parcel in) {
        NetworkID = in.readString();
        BranchID = in.readString();
        BranchName = in.readString();
        BranchType = in.readString();
        BranchGroup = in.readString();
        AuthorisedRepresentative = in.readString();
        StreetAddress = in.readString();
        City = in.readString();
        Province = in.readString();
        Zip = in.readInt();
        Country = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
        AuthorisedEmailAddress = in.readString();
        AuthorisedTelNo = in.readString();
        AuthorisedCountryCode = in.readString();
        AuthorisedMobileNo = in.readString();
        Fax = in.readString();
        CurrentOrderCount = in.readInt();
        DateTimeAdded = in.readString();
        Status = in.readString();
        CalculatedDistance = in.readDouble();
        StoreHoursStart = in.readString();
        StoreHoursEnd = in.readString();
    }

    public static final Creator<Branches> CREATOR = new Creator<Branches>() {
        @Override
        public Branches createFromParcel(Parcel in) {
            return new Branches(in);
        }

        @Override
        public Branches[] newArray(int size) {
            return new Branches[size];
        }
    };

    public String getNetworkID() {
        return NetworkID;
    }

    public void setNetworkID(String networkID) {
        NetworkID = networkID;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getBranchType() {
        return BranchType;
    }

    public void setBranchType(String branchType) {
        BranchType = branchType;
    }

    public String getBranchGroup() {
        return BranchGroup;
    }

    public void setBranchGroup(String branchGroup) {
        BranchGroup = branchGroup;
    }

    public String getAuthorisedRepresentative() {
        return AuthorisedRepresentative;
    }

    public void setAuthorisedRepresentative(String authorisedRepresentative) {
        AuthorisedRepresentative = authorisedRepresentative;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public int getZip() {
        return Zip;
    }

    public void setZip(int zip) {
        Zip = zip;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getAuthorisedEmailAddress() {
        return AuthorisedEmailAddress;
    }

    public void setAuthorisedEmailAddress(String authorisedEmailAddress) {
        AuthorisedEmailAddress = authorisedEmailAddress;
    }

    public String getAuthorisedTelNo() {
        return AuthorisedTelNo;
    }

    public void setAuthorisedTelNo(String authorisedTelNo) {
        AuthorisedTelNo = authorisedTelNo;
    }

    public String getAuthorisedCountryCode() {
        return AuthorisedCountryCode;
    }

    public void setAuthorisedCountryCode(String authorisedCountryCode) {
        AuthorisedCountryCode = authorisedCountryCode;
    }

    public String getAuthorisedMobileNo() {
        return AuthorisedMobileNo;
    }

    public void setAuthorisedMobileNo(String authorisedMobileNo) {
        AuthorisedMobileNo = authorisedMobileNo;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public int getCurrentOrderCount() {
        return CurrentOrderCount;
    }

    public void setCurrentOrderCount(int currentOrderCount) {
        CurrentOrderCount = currentOrderCount;
    }

    public String getDateTimeAdded() {
        return DateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        DateTimeAdded = dateTimeAdded;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getCalculatedDistance() {
        return CalculatedDistance;
    }

    public void setCalculatedDistance(double calculatedDistance) {
        CalculatedDistance = calculatedDistance;
    }

    public void setStoreHoursStart(String storeHoursStart) {
        StoreHoursStart = storeHoursStart;
    }

    public void setStoreHoursEnd(String storeHoursEnd) {
        StoreHoursEnd = storeHoursEnd;
    }

    public String getStoreHoursStart() {
        return StoreHoursStart;
    }

    public String getStoreHoursEnd() {
        return StoreHoursEnd;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(NetworkID);
        dest.writeString(BranchID);
        dest.writeString(BranchName);
        dest.writeString(BranchType);
        dest.writeString(BranchGroup);
        dest.writeString(AuthorisedRepresentative);
        dest.writeString(StreetAddress);
        dest.writeString(City);
        dest.writeString(Province);
        dest.writeInt(Zip);
        dest.writeString(Country);
        dest.writeString(Longitude);
        dest.writeString(Latitude);
        dest.writeString(AuthorisedEmailAddress);
        dest.writeString(AuthorisedTelNo);
        dest.writeString(AuthorisedCountryCode);
        dest.writeString(AuthorisedMobileNo);
        dest.writeString(Fax);
        dest.writeInt(CurrentOrderCount);
        dest.writeString(DateTimeAdded);
        dest.writeString(Status);
        dest.writeDouble(CalculatedDistance);
        dest.writeString(StoreHoursStart);
        dest.writeString(StoreHoursEnd);
    }

}
