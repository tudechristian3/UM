package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Variations implements Parcelable {

    private String itemImage,itemName,itemDescription,itemQuantity;
    private double itemAmount,itemTotalPrice;

    public Variations() {
        super();
    }

    public Variations(String itemImage, String itemName, String itemDescription, String itemQuantity, double itemAmount, double itemTotalPrice) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemQuantity = itemQuantity;
        this.itemAmount = itemAmount;
        this.itemTotalPrice = itemTotalPrice;
    }

    private Variations(Parcel in) {
        itemImage = in.readString();
        itemName = in.readString();
        itemDescription = in.readString();
        itemQuantity = in.readString();
        itemAmount = in.readDouble();
        itemTotalPrice = in.readDouble();
    }

    public static final Creator<Variations> CREATOR = new Creator<Variations>() {
        @Override
        public Variations createFromParcel(Parcel in) {
            return new Variations(in);
        }

        @Override
        public Variations[] newArray(int size) {
            return new Variations[size];
        }
    };

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemImage);
        dest.writeString(itemName);
        dest.writeString(itemDescription);
        dest.writeString(itemQuantity);
        dest.writeDouble(itemAmount);
        dest.writeDouble(itemTotalPrice);
    }
}
