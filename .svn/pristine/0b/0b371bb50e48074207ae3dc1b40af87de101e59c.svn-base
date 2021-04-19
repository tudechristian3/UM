package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 5/29/2017.
 */
public class MyList implements Parcelable {
    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;
    @SerializedName("CustomerType")
    @Expose
    private String CustomerType;
    @SerializedName("IMEI")
    @Expose
    private String IMEI;
    @SerializedName("Category")
    @Expose
    private String Category;
    @SerializedName("CatClass")
    @Expose
    private String CatClass;
    @SerializedName("ItemPicUrl")
    @Expose
    private String ItemPicUrl;
    @SerializedName("DateTimeAdded")
    @Expose
    private String DateTimeAdded;
    @SerializedName("isRegistered")
    @Expose
    private int isRegistered;
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("ItemCode")
    @Expose
    private String ItemCode;
    @SerializedName("ItemDescription")
    @Expose
    private String ItemDescription;
    @SerializedName("Type")
    @Expose
    private String Type;

    public MyList(String customerID, String customerType, String IMEI, String category, String catClass, String itemPicUrl, String dateTimeAdded, int isRegistered, String description, String itemCode, String itemDescription, String type) {
        CustomerID = customerID;
        CustomerType = customerType;
        this.IMEI = IMEI;
        Category = category;
        CatClass = catClass;
        ItemPicUrl = itemPicUrl;
        DateTimeAdded = dateTimeAdded;
        this.isRegistered = isRegistered;
        Description = description;
        ItemCode = itemCode;
        ItemDescription = itemDescription;
        Type = type;
    }

    protected MyList(Parcel in) {
        CustomerID = in.readString();
        CustomerType = in.readString();
        IMEI = in.readString();
        Category = in.readString();
        CatClass = in.readString();
        ItemPicUrl = in.readString();
        DateTimeAdded = in.readString();
        isRegistered = in.readInt();
        Description = in.readString();
        ItemCode = in.readString();
        ItemDescription = in.readString();
        Type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CustomerID);
        dest.writeString(CustomerType);
        dest.writeString(IMEI);
        dest.writeString(Category);
        dest.writeString(CatClass);
        dest.writeString(ItemPicUrl);
        dest.writeString(DateTimeAdded);
        dest.writeInt(isRegistered);
        dest.writeString(Description);
        dest.writeString(ItemCode);
        dest.writeString(ItemDescription);
        dest.writeString(Type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyList> CREATOR = new Creator<MyList>() {
        @Override
        public MyList createFromParcel(Parcel in) {
            return new MyList(in);
        }

        @Override
        public MyList[] newArray(int size) {
            return new MyList[size];
        }
    };

    public String getCustomerID() {
        return CustomerID;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public String getIMEI() {
        return IMEI;
    }

    public String getCategory() {
        return Category;
    }

    public String getCatClass() {
        return CatClass;
    }

    public String getItemPicUrl() {
        return ItemPicUrl;
    }

    public String getDateTimeAdded() {
        return DateTimeAdded;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public String getDescription() {
        return Description;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getType() {
        return Type;
    }
}
