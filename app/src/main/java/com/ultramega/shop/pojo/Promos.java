package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 9/8/2017.
 */

public class Promos implements Parcelable {
    @SerializedName("PromoID")
    @Expose
    private String PromoID;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("PhotoPath")
    @Expose
    private String PhotoPath;
    @SerializedName("isPromo")
    @Expose
    private int isPromo;
    @SerializedName("Rank")
    @Expose
    private int Rank;

    public Promos(String promoID, String name, String photoPath, int isPromo, int rank) {
        PromoID = promoID;
        Name = name;
        PhotoPath = photoPath;
        this.isPromo = isPromo;
        Rank = rank;
    }

    protected Promos(Parcel in) {
        PromoID = in.readString();
        Name = in.readString();
        PhotoPath = in.readString();
        isPromo = in.readInt();
        Rank = in.readInt();
    }

    public static final Creator<Promos> CREATOR = new Creator<Promos>() {
        @Override
        public Promos createFromParcel(Parcel in) {
            return new Promos(in);
        }

        @Override
        public Promos[] newArray(int size) {
            return new Promos[size];
        }
    };

    public String getPromoID() {
        return PromoID;
    }

    public String getName() {
        return Name;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public int getIsPromo() {
        return isPromo;
    }

    public int getRank() {
        return Rank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(PromoID);
        parcel.writeString(Name);
        parcel.writeString(PhotoPath);
        parcel.writeInt(isPromo);
        parcel.writeInt(Rank);
    }
}
