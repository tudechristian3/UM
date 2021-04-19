package com.ultramega.shop.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerWallet implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("ConsumerID")
    @Expose
    private String ConsumerID;
    @SerializedName("CurrencyID")
    @Expose
    private String CurrencyID;
    @SerializedName("TotalWallet")
    @Expose
    private double TotalWallet;
    @SerializedName("WalletType")
    @Expose
    private String WalletType;
    @SerializedName("LastWalletReloadDate")
    @Expose
    private String LastWalletReloadDate;
    @SerializedName("LastWalletReloadAmount")
    @Expose
    private double LastWalletReloadAmount;
    @SerializedName("LastWalletReloadPreWallet")
    @Expose
    private double LastWalletReloadPreWallet;
    @SerializedName("LastWalletReloadPostWallet")
    @Expose
    private double LastWalletReloadPostWallet;
    @SerializedName("LastOrderPaymentDate")
    @Expose
    private String LastOrderPaymentDate;
    @SerializedName("LastOrderPaymentAmount")
    @Expose
    private double LastOrderPaymentAmount;
    @SerializedName("LastOrderPaymentPreWallet")
    @Expose
    private double LastOrderPaymentPreWallet;
    @SerializedName("LastOrderPaymentPostWallet")
    @Expose
    private double LastOrderPaymentPostWallet;
    @SerializedName("Status")
    @Expose
    private String Status;
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

    public ConsumerWallet(){
        super();
    }

    public ConsumerWallet(int ID, String consumerID, String currencyID, double totalWallet, String walletType, String lastWalletReloadDate, double lastWalletReloadAmount, double lastWalletReloadPreWallet, double lastWalletReloadPostWallet, String lastOrderPaymentDate, double lastOrderPaymentAmount, double lastOrderPaymentPreWallet, double lastOrderPaymentPostWallet, String status, String extra1, String extra2, String extra3, String extra4, String notes1, String notes2) {
        this.ID = ID;
        ConsumerID = consumerID;
        CurrencyID = currencyID;
        TotalWallet = totalWallet;
        WalletType = walletType;
        LastWalletReloadDate = lastWalletReloadDate;
        LastWalletReloadAmount = lastWalletReloadAmount;
        LastWalletReloadPreWallet = lastWalletReloadPreWallet;
        LastWalletReloadPostWallet = lastWalletReloadPostWallet;
        LastOrderPaymentDate = lastOrderPaymentDate;
        LastOrderPaymentAmount = lastOrderPaymentAmount;
        LastOrderPaymentPreWallet = lastOrderPaymentPreWallet;
        LastOrderPaymentPostWallet = lastOrderPaymentPostWallet;
        Status = status;
        Extra1 = extra1;
        Extra2 = extra2;
        Extra3 = extra3;
        Extra4 = extra4;
        Notes1 = notes1;
        Notes2 = notes2;
    }

    private ConsumerWallet(Parcel in) {
        ID = in.readInt();
        ConsumerID = in.readString();
        CurrencyID = in.readString();
        TotalWallet = in.readDouble();
        WalletType = in.readString();
        LastWalletReloadDate = in.readString();
        LastWalletReloadAmount = in.readDouble();
        LastWalletReloadPreWallet = in.readDouble();
        LastWalletReloadPostWallet = in.readDouble();
        LastOrderPaymentDate = in.readString();
        LastOrderPaymentAmount = in.readDouble();
        LastOrderPaymentPreWallet = in.readDouble();
        LastOrderPaymentPostWallet = in.readDouble();
        Status = in.readString();
        Extra1 = in.readString();
        Extra2 = in.readString();
        Extra3 = in.readString();
        Extra4 = in.readString();
        Notes1 = in.readString();
        Notes2 = in.readString();
    }

    public static final Creator<ConsumerWallet> CREATOR = new Creator<ConsumerWallet>() {
        @Override
        public ConsumerWallet createFromParcel(Parcel in) {
            return new ConsumerWallet(in);
        }

        @Override
        public ConsumerWallet[] newArray(int size) {
            return new ConsumerWallet[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getConsumerID() {
        return ConsumerID;
    }

    public void setConsumerID(String consumerID) {
        ConsumerID = consumerID;
    }

    public String getCurrencyID() {
        return CurrencyID;
    }

    public void setCurrencyID(String currencyID) {
        CurrencyID = currencyID;
    }

    public double getTotalWallet() {
        return TotalWallet;
    }

    public void setTotalWallet(double totalWallet) {
        TotalWallet = totalWallet;
    }

    public String getWalletType() {
        return WalletType;
    }

    public void setWalletType(String walletType) {
        WalletType = walletType;
    }

    public String getLastWalletReloadDate() {
        return LastWalletReloadDate;
    }

    public void setLastWalletReloadDate(String lastWalletReloadDate) {
        LastWalletReloadDate = lastWalletReloadDate;
    }

    public double getLastWalletReloadAmount() {
        return LastWalletReloadAmount;
    }

    public void setLastWalletReloadAmount(double lastWalletReloadAmount) {
        LastWalletReloadAmount = lastWalletReloadAmount;
    }

    public double getLastWalletReloadPreWallet() {
        return LastWalletReloadPreWallet;
    }

    public void setLastWalletReloadPreWallet(double lastWalletReloadPreWallet) {
        LastWalletReloadPreWallet = lastWalletReloadPreWallet;
    }

    public double getLastWalletReloadPostWallet() {
        return LastWalletReloadPostWallet;
    }

    public void setLastWalletReloadPostWallet(double lastWalletReloadPostWallet) {
        LastWalletReloadPostWallet = lastWalletReloadPostWallet;
    }

    public String getLastOrderPaymentDate() {
        return LastOrderPaymentDate;
    }

    public void setLastOrderPaymentDate(String lastOrderPaymentDate) {
        LastOrderPaymentDate = lastOrderPaymentDate;
    }

    public double getLastOrderPaymentAmount() {
        return LastOrderPaymentAmount;
    }

    public void setLastOrderPaymentAmount(double lastOrderPaymentAmount) {
        LastOrderPaymentAmount = lastOrderPaymentAmount;
    }

    public double getLastOrderPaymentPreWallet() {
        return LastOrderPaymentPreWallet;
    }

    public void setLastOrderPaymentPreWallet(double lastOrderPaymentPreWallet) {
        LastOrderPaymentPreWallet = lastOrderPaymentPreWallet;
    }

    public double getLastOrderPaymentPostWallet() {
        return LastOrderPaymentPostWallet;
    }

    public void setLastOrderPaymentPostWallet(double lastOrderPaymentPostWallet) {
        LastOrderPaymentPostWallet = lastOrderPaymentPostWallet;
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
        dest.writeString(ConsumerID);
        dest.writeString(CurrencyID);
        dest.writeDouble(TotalWallet);
        dest.writeString(WalletType);
        dest.writeString(LastWalletReloadDate);
        dest.writeDouble(LastWalletReloadAmount);
        dest.writeDouble(LastWalletReloadPreWallet);
        dest.writeDouble(LastWalletReloadPostWallet);
        dest.writeString(LastOrderPaymentDate);
        dest.writeDouble(LastOrderPaymentAmount);
        dest.writeDouble(LastOrderPaymentPreWallet);
        dest.writeDouble(LastOrderPaymentPostWallet);
        dest.writeString(Status);
        dest.writeString(Extra1);
        dest.writeString(Extra2);
        dest.writeString(Extra3);
        dest.writeString(Extra4);
        dest.writeString(Notes1);
        dest.writeString(Notes2);
    }
}
