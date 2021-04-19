package com.ultramega.shop.pojo;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by User-PC on 8/10/2017.
 */

public class SearchAddress implements SearchSuggestion {
    private String location;
    private int locationIndex;

    public SearchAddress(String location, int locationIndex) {
        this.location = location;
        this.locationIndex = locationIndex;
    }

    public SearchAddress(Parcel source) {
        this.location = source.readString();
        this.locationIndex = source.readInt();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    @Override
    public String getBody() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeInt(locationIndex);
    }
}
