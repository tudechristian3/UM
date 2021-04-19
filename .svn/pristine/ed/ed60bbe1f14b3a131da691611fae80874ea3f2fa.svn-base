package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Supplier implements Parcelable {

    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("SupplierID")
    @Expose
    private String SupplierID;
    @SerializedName("SupplierName")
    @Expose
    private String SupplierName;
    @SerializedName("Rank")
    @Expose
    private int Rank;
    @SerializedName("isSponsor")
    @Expose
    private int isSponsor;
    @SerializedName("PhotoPath")
    @Expose
    private String PhotoPath;

    protected Supplier(Parcel in) {
        ID = in.readInt();
        SupplierID = in.readString();
        SupplierName = in.readString();
        Rank = in.readInt();
        isSponsor = in.readInt();
        PhotoPath = in.readString();
    }

    public static final Creator<Supplier> CREATOR = new Creator<Supplier>() {
        @Override
        public Supplier createFromParcel(Parcel in) {
            return new Supplier(in);
        }

        @Override
        public Supplier[] newArray(int size) {
            return new Supplier[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public int getRank() {
        return Rank;
    }

    public int getIsSponsor() {
        return isSponsor;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public Supplier(int ID, String supplierID, String supplierName, int rank, int isSponsor, String photoPath) {
        this.ID = ID;
        SupplierID = supplierID;
        SupplierName = supplierName;
        Rank = rank;
        this.isSponsor = isSponsor;
        PhotoPath = photoPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(SupplierID);
        dest.writeString(SupplierName);
        dest.writeInt(Rank);
        dest.writeInt(isSponsor);
        dest.writeString(PhotoPath);
    }
}
