package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerBehaviourPattern implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("DateTimeIN")
    @Expose
    private String DateTimeIN;
    @SerializedName("ConsumerID")
    @Expose
    private String ConsumerID;
    @SerializedName("CategoryID")
    @Expose
    private String CategoryID;
    @SerializedName("CategoryName")
    @Expose
    private String CategoryName;
    @SerializedName("Extra1")
    @Expose
    private String Extra1;
    @SerializedName("Extra2")
    @Expose
    private String Extra2;
    @SerializedName("Extra3")
    @Expose
    private String Extra3;
    @SerializedName("Extra4")
    @Expose
    private String Extra4;
    @SerializedName("Notes1")
    @Expose
    private String Notes1;
    @SerializedName("Notes2")
    @Expose
    private String Notes2;

    public ConsumerBehaviourPattern(){
        super();
    }

    public ConsumerBehaviourPattern(int ID, String dateTimeIN, String consumerID, String categoryID, String categoryName, String extra1, String extra2, String extra3, String extra4, String notes1, String notes2) {
        this.ID = ID;
        DateTimeIN = dateTimeIN;
        ConsumerID = consumerID;
        CategoryID = categoryID;
        CategoryName = categoryName;
        Extra1 = extra1;
        Extra2 = extra2;
        Extra3 = extra3;
        Extra4 = extra4;
        Notes1 = notes1;
        Notes2 = notes2;
    }

    private ConsumerBehaviourPattern(Parcel in) {
        ID = in.readInt();
        DateTimeIN = in.readString();
        ConsumerID = in.readString();
        CategoryID = in.readString();
        CategoryName = in.readString();
        Extra1 = in.readString();
        Extra2 = in.readString();
        Extra3 = in.readString();
        Extra4 = in.readString();
        Notes1 = in.readString();
        Notes2 = in.readString();
    }

    public static final Creator<ConsumerBehaviourPattern> CREATOR = new Creator<ConsumerBehaviourPattern>() {
        @Override
        public ConsumerBehaviourPattern createFromParcel(Parcel in) {
            return new ConsumerBehaviourPattern(in);
        }

        @Override
        public ConsumerBehaviourPattern[] newArray(int size) {
            return new ConsumerBehaviourPattern[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDateTimeIN() {
        return DateTimeIN;
    }

    public void setDateTimeIN(String dateTimeIN) {
        DateTimeIN = dateTimeIN;
    }

    public String getConsumerID() {
        return ConsumerID;
    }

    public void setConsumerID(String consumerID) {
        ConsumerID = consumerID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getExtra1() {
        return Extra1;
    }

    public void setExtra1(String extra1) {
        Extra1 = extra1;
    }

    public String getExtra2() {
        return Extra2;
    }

    public void setExtra2(String extra2) {
        Extra2 = extra2;
    }

    public String getExtra3() {
        return Extra3;
    }

    public void setExtra3(String extra3) {
        Extra3 = extra3;
    }

    public String getExtra4() {
        return Extra4;
    }

    public void setExtra4(String extra4) {
        Extra4 = extra4;
    }

    public String getNotes1() {
        return Notes1;
    }

    public void setNotes1(String notes1) {
        Notes1 = notes1;
    }

    public String getNotes2() {
        return Notes2;
    }

    public void setNotes2(String notes2) {
        Notes2 = notes2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(DateTimeIN);
        dest.writeString(ConsumerID);
        dest.writeString(CategoryID);
        dest.writeString(CategoryName);
        dest.writeString(Extra1);
        dest.writeString(Extra2);
        dest.writeString(Extra3);
        dest.writeString(Extra4);
        dest.writeString(Notes1);
        dest.writeString(Notes2);
    }
}
