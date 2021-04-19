package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankReference implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("NetworkID")
    @Expose
    private String NetworkID;
    @SerializedName("BankCode")
    @Expose
    private String BankCode;
    @SerializedName("BankName")
    @Expose
    private String BankName;
    @SerializedName("BankAccountNo")
    @Expose
    private String BankAccountNo;
    @SerializedName("BankAccountName")
    @Expose
    private String BankAccountName;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Extra1")
    @Expose
    private String Extra1;
    @SerializedName("Extra2")
    @Expose
    private String Extra2;
    @SerializedName("Notes1")
    @Expose
    private String Notes1;
    @SerializedName("Notes2")
    @Expose
    private String Notes2;

    public BankReference(){
        super();
    }

    public BankReference(int ID, String networkID, String bankCode, String bankName, String bankAccountNo, String bankAccountName, String status, String extra1, String extra2, String notes1, String notes2) {
        this.ID = ID;
        NetworkID = networkID;
        BankCode = bankCode;
        BankName = bankName;
        BankAccountNo = bankAccountNo;
        BankAccountName = bankAccountName;
        Status = status;
        Extra1 = extra1;
        Extra2 = extra2;
        Notes1 = notes1;
        Notes2 = notes2;
    }

    private BankReference(Parcel in) {
        ID = in.readInt();
        NetworkID = in.readString();
        BankCode = in.readString();
        BankName = in.readString();
        BankAccountNo = in.readString();
        BankAccountName = in.readString();
        Status = in.readString();
        Extra1 = in.readString();
        Extra2 = in.readString();
        Notes1 = in.readString();
        Notes2 = in.readString();
    }

    public static final Creator<BankReference> CREATOR = new Creator<BankReference>() {
        @Override
        public BankReference createFromParcel(Parcel in) {
            return new BankReference(in);
        }

        @Override
        public BankReference[] newArray(int size) {
            return new BankReference[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNetworkID() {
        return NetworkID;
    }

    public void setNetworkID(String networkID) {
        NetworkID = networkID;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankAccountNo() {
        return BankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        BankAccountNo = bankAccountNo;
    }

    public String getBankAccountName() {
        return BankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        BankAccountName = bankAccountName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
        dest.writeString(NetworkID);
        dest.writeString(BankCode);
        dest.writeString(BankName);
        dest.writeString(BankAccountNo);
        dest.writeString(BankAccountName);
        dest.writeString(Status);
        dest.writeString(Extra1);
        dest.writeString(Extra2);
        dest.writeString(Notes1);
        dest.writeString(Notes2);
    }

    @Override
    public String toString() {
        return BankName;
    }
}
