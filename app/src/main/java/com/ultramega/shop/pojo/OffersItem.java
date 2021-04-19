package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class OffersItem implements Parcelable {

    private String image, title, price;

    public OffersItem() {
        super();
    }

    public OffersItem(String image, String title, String price) {
        this.image = image;
        this.title = title;
        this.price = price;
    }

    private OffersItem(Parcel in) {
        image = in.readString();
        title = in.readString();
        price = in.readString();
    }

    public static final Creator<OffersItem> CREATOR = new Creator<OffersItem>() {
        @Override
        public OffersItem createFromParcel(Parcel in) {
            return new OffersItem(in);
        }

        @Override
        public OffersItem[] newArray(int size) {
            return new OffersItem[size];
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(price);
    }
}
