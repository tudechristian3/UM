package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItems implements Parcelable {
    @SerializedName("PromoID")
    @Expose
    private String PromoID;
    @SerializedName("Category")
    @Expose
    private String Category;
    @SerializedName("CatClass")
    @Expose
    private String CatClass;
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("DateTimeIN")
    @Expose
    private String DateTimeIN;
    @SerializedName("ItemGroupPicURL")
    @Expose
    private String ItemGroupPicURL;
    @SerializedName("isPromo")
    @Expose
    private int isPromo;

    public CategoryItems(String category, String catClass, String description, String itemGroupPicURL) {
        Category = category;
        CatClass = catClass;
        Description = description;
        ItemGroupPicURL = itemGroupPicURL;
    }

    public CategoryItems(String category, String catClass, String description, String dateTimeIN, String itemGroupPicURL) {
        Category = category;
        CatClass = catClass;
        Description = description;
        DateTimeIN = dateTimeIN;
        ItemGroupPicURL = itemGroupPicURL;
    }

    public CategoryItems(String description) {
        Description = description;
    }

    public CategoryItems(String description, String itemGroupPicURL) {
        Description = description;
        ItemGroupPicURL = itemGroupPicURL;
    }

    public CategoryItems(String category, String catClass, String description, String dateTimeIN, String itemGroupPicURL, int isPromo) {
        Category = category;
        CatClass = catClass;
        Description = description;
        DateTimeIN = dateTimeIN;
        ItemGroupPicURL = itemGroupPicURL;
        this.isPromo = isPromo;
    }

    public CategoryItems(String category, String catClass, String description, String itemGroupPicURL, int isPromo) {
        Category = category;
        CatClass = catClass;
        Description = description;
        ItemGroupPicURL = itemGroupPicURL;
        this.isPromo = isPromo;
    }

    public CategoryItems(String promoID, String category, String catClass, String description, String dateTimeIN, String itemGroupPicURL, int isPromo) {
        PromoID = promoID;
        Category = category;
        CatClass = catClass;
        Description = description;
        DateTimeIN = dateTimeIN;
        ItemGroupPicURL = itemGroupPicURL;
        this.isPromo = isPromo;
    }

    protected CategoryItems(Parcel in) {
        PromoID = in.readString();
        Category = in.readString();
        CatClass = in.readString();
        Description = in.readString();
        DateTimeIN = in.readString();
        ItemGroupPicURL = in.readString();
        isPromo = in.readInt();
    }

    public static final Creator<CategoryItems> CREATOR = new Creator<CategoryItems>() {
        @Override
        public CategoryItems createFromParcel(Parcel in) {
            return new CategoryItems(in);
        }

        @Override
        public CategoryItems[] newArray(int size) {
            return new CategoryItems[size];
        }
    };

    public String getPromoID() {
        return PromoID;
    }

    public String getCategory() {
        return Category;
    }

    public String getCatClass() {
        return CatClass;
    }

    public String getDescription() {
        return Description;
    }

    public String getDateTimeIN() {
        return DateTimeIN;
    }

    public void setItemGroupPicURL(String itemGroupPicURL) {
        ItemGroupPicURL = itemGroupPicURL;
    }

    public String getItemGroupPicURL() {
        return ItemGroupPicURL;
    }

    public int getIsPromo() {
        return isPromo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(PromoID);
        parcel.writeString(Category);
        parcel.writeString(CatClass);
        parcel.writeString(Description);
        parcel.writeString(DateTimeIN);
        parcel.writeString(ItemGroupPicURL);
        parcel.writeInt(isPromo);
    }
}
