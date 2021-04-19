package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;

/**
 * Created by User on 04/09/2017.
 */

public class WalletReload implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("PaymentTxnNo")
    @Expose
    private String PaymentTxnNo;
    @SerializedName("PartnerPaymentTxnNo")
    @Expose
    private String PartnerPaymentTxnNo;
    @SerializedName("DateTimeIN")
    @Expose
    private String DateTimeIN;
    @SerializedName("DateTimeCompleted")
    @Expose
    private String DateTimeCompleted;
    @SerializedName("ConsumerType")
    @Expose
    private String ConsumerType;
    @SerializedName("ConsumerName")
    @Expose
    private String ConsumerName;
    @SerializedName("ConsumerMobileNumber")
    @Expose
    private String ConsumerMobileNumber;
    @SerializedName("AmountPaid")
    @Expose
    private String AmountPaid;
    @SerializedName("PaymentOption")
    @Expose
    private String PaymentOption;
    @SerializedName("PaymentDetails")
    @Expose
    private String PaymentDetails;
    @SerializedName("PreAccountWallet")
    @Expose
    private String PreAccountWallet;
    @SerializedName("PostAccountWallet")
    @Expose
    private String PostAccountWallet;
    @SerializedName("CustomerRemarks")
    @Expose
    private String CustomerRemarks;
    @SerializedName("ActedBy")
    @Expose
    private String ActedBy;
    @SerializedName("Remarks")
    @Expose
    private String Remarks;
    @SerializedName("Status")
    @Expose
    private String Status;

    public WalletReload(Parcel in) {
        this.ID = in.readInt();
        this.PaymentTxnNo = in.readString();
        this.PartnerPaymentTxnNo = in.readString();
        this.DateTimeIN = in.readString();
        this.DateTimeCompleted = in.readString();
        this.ConsumerType = in.readString();
        this.ConsumerName = in.readString();
        this.ConsumerMobileNumber = in.readString();
        this.AmountPaid = in.readString();
        this.PaymentOption = in.readString();
        this.PaymentDetails = in.readString();
        this.PreAccountWallet = in.readString();
        this.PostAccountWallet = in.readString();
        this.CustomerRemarks = in.readString();
        this.ActedBy = in.readString();
        this.Remarks = in.readString();
        this.Status = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(PaymentTxnNo);
        dest.writeString(PartnerPaymentTxnNo);
        dest.writeString(DateTimeIN);
        dest.writeString(DateTimeCompleted);
        dest.writeString(ConsumerType);
        dest.writeString(ConsumerName);
        dest.writeString(ConsumerMobileNumber);
        dest.writeString(AmountPaid);
        dest.writeString(PaymentOption);
        dest.writeString(PaymentDetails);
        dest.writeString(PreAccountWallet);
        dest.writeString(PostAccountWallet);
        dest.writeString(CustomerRemarks);
        dest.writeString(ActedBy);
        dest.writeString(Remarks);
        dest.writeString(Status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalletReload> CREATOR = new Creator<WalletReload>() {
        @Override
        public WalletReload createFromParcel(Parcel in) {
            return new WalletReload(in);
        }

        @Override
        public WalletReload[] newArray(int size) {
            return new WalletReload[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getPaymentTxnNo() {
        return PaymentTxnNo;
    }

    public String getPartnerPaymentTxnNo() {
        return PartnerPaymentTxnNo;
    }

    public String getDateTimeIN() {
        return DateTimeIN;
    }

    public String getDateTimeCompleted() {
        return DateTimeCompleted;
    }

    public String getConsumerType() {
        return ConsumerType;
    }

    public String getConsumerName() {
        return ConsumerName;
    }

    public String getConsumerMobileNumber() {
        return ConsumerMobileNumber;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public String getPaymentOption() {
        return PaymentOption;
    }

    public String getPaymentDetails() {
        return PaymentDetails;
    }

    public String getPreAccountWallet() {
        return PreAccountWallet;
    }

    public String getPostAccountWallet() {
        return PostAccountWallet;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public String getActedBy() {
        return ActedBy;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getStatus() {
        return Status;
    }


    public WalletReload(int ID, String paymentTxnNo, String partnerPaymentTxnNo, String dateTimeIN, String dateTimeCompleted, String consumerType, String consumerName, String consumerMobileNumber, String amountPaid, String paymentOption, String paymentDetails, String preAccountWallet, String postAccountWallet, String customerRemarks, String actedBy, String remarks, String status) {
        this.ID = ID;
        PaymentTxnNo = paymentTxnNo;
        PartnerPaymentTxnNo = partnerPaymentTxnNo;
        DateTimeIN = dateTimeIN;
        DateTimeCompleted = dateTimeCompleted;
        ConsumerType = consumerType;
        ConsumerName = consumerName;
        ConsumerMobileNumber = consumerMobileNumber;
        AmountPaid = amountPaid;
        PaymentOption = paymentOption;
        PaymentDetails = paymentDetails;
        PreAccountWallet = preAccountWallet;
        PostAccountWallet = postAccountWallet;
        CustomerRemarks = customerRemarks;
        ActedBy = actedBy;
        Remarks = remarks;
        Status = status;
    }
}
