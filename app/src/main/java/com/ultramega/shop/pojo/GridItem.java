package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User-PC on 5/24/2017.
 */
public class GridItem implements Parcelable {
    private String image;
    private String title;

    public GridItem() {
        super();
    }

    public GridItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    private GridItem(Parcel in) {
        image = in.readString();
        title = in.readString();
    }

    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel in) {
            return new GridItem(in);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
    }
}
