package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 8/25/2017.
 */

public class SupplierItems implements Parcelable {
    @SerializedName("Category")
    @Expose
    private String Category;
    @SerializedName("CatClass")
    @Expose
    private String CatClass;
    @SerializedName("SupplierID")
    @Expose
    private String SupplierID;
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("PhotoURL")
    @Expose
    private String PhotoURL;
    @SerializedName("isPromo")
    @Expose
    private int isPromo;

    public SupplierItems(String category, String catClass, String supplierID, String description, String photoURL, int isPromo) {
        Category = category;
        CatClass = catClass;
        SupplierID = supplierID;
        Description = description;
        PhotoURL = photoURL;
        this.isPromo = isPromo;
    }

    protected SupplierItems(Parcel in) {
        Category = in.readString();
        CatClass = in.readString();
        SupplierID = in.readString();
        Description = in.readString();
        PhotoURL = in.readString();
        isPromo = in.readInt();
    }

    public static final Creator<SupplierItems> CREATOR = new Creator<SupplierItems>() {
        @Override
        public SupplierItems createFromParcel(Parcel in) {
            return new SupplierItems(in);
        }

        @Override
        public SupplierItems[] newArray(int size) {
            return new SupplierItems[size];
        }
    };

    public String getCategory() {
        return Category;
    }

    public String getCatClass() {
        return CatClass;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public String getDescription() {
        return Description;
    }

    public String getPhotoURL() {
        return PhotoURL;
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
        parcel.writeString(Category);
        parcel.writeString(CatClass);
        parcel.writeString(SupplierID);
        parcel.writeString(Description);
        parcel.writeString(PhotoURL);
        parcel.writeInt(isPromo);
    }
}
