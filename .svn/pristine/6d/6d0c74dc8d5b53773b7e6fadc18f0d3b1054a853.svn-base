package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ban_Lenovo on 9/18/2017.
 */

public class PesoPay implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("MerchantID")
    @Expose
    private String MerchantID;
    @SerializedName("CurrencyCode")
    @Expose
    private String CurrencyCode;
    @SerializedName("SuccessURL")
    @Expose
    private String SuccessURL;
    @SerializedName("FailedURL")
    @Expose
    private String FailedURL;
    @SerializedName("CancelledURL")
    @Expose
    private String CancelledURL;
    @SerializedName("PaymentType")
    @Expose
    private String PaymentType;
    @SerializedName("Language")
    @Expose
    private String Language;
    @SerializedName("PaymentMethod")
    @Expose
    private String PaymentMethod;
    @SerializedName("SecretKey")
    @Expose
    private String SecretKey;
    @SerializedName("PesoPayURL")
    @Expose
    private String PesoPayURL;

    public PesoPay(int ID, String merchantID, String currencyCode, String successURL, String failedURL, String cancelledURL, String paymentType, String language, String paymentMethod, String secretKey, String pesoPayURL) {
        this.ID = ID;
        MerchantID = merchantID;
        CurrencyCode = currencyCode;
        SuccessURL = successURL;
        FailedURL = failedURL;
        CancelledURL = cancelledURL;
        PaymentType = paymentType;
        Language = language;
        PaymentMethod = paymentMethod;
        SecretKey = secretKey;
        PesoPayURL = pesoPayURL;
    }

    protected PesoPay(Parcel in) {
        ID = in.readInt();
        MerchantID = in.readString();
        CurrencyCode = in.readString();
        SuccessURL = in.readString();
        FailedURL = in.readString();
        CancelledURL = in.readString();
        PaymentType = in.readString();
        Language = in.readString();
        PaymentMethod = in.readString();
        SecretKey = in.readString();
        PesoPayURL = in.readString();
    }

    public static final Creator<PesoPay> CREATOR = new Creator<PesoPay>() {
        @Override
        public PesoPay createFromParcel(Parcel in) {
            return new PesoPay(in);
        }

        @Override
        public PesoPay[] newArray(int size) {
            return new PesoPay[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public String getSuccessURL() {
        return SuccessURL;
    }

    public String getFailedURL() {
        return FailedURL;
    }

    public String getCancelledURL() {
        return CancelledURL;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public String getLanguage() {
        return Language;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public String getPesoPayURL() {
        return PesoPayURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(MerchantID);
        parcel.writeString(CurrencyCode);
        parcel.writeString(SuccessURL);
        parcel.writeString(FailedURL);
        parcel.writeString(CancelledURL);
        parcel.writeString(PaymentType);
        parcel.writeString(Language);
        parcel.writeString(PaymentMethod);
        parcel.writeString(SecretKey);
        parcel.writeString(PesoPayURL);
    }
}
