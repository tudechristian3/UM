package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 18/09/2017.
 */

public class Notification implements Parcelable {
    @SerializedName("DateTimeIN")
    @Expose
    private String DateTimeIN;
    @SerializedName("TxnNo")
    @Expose
    private String TxnNo;
    @SerializedName("Channel")
    @Expose
    private String Channel;
    @SerializedName("Subject")
    @Expose
    private String Subject;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("Sender")
    @Expose
    private String Sender;
    @SerializedName("SenderLogo")
    @Expose
    private String SenderLogo;
    @SerializedName("PayLoad")
    @Expose
    private String PayLoad;
    @SerializedName("ReadStatus")
    @Expose
    private String ReadStatus;
    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;

    public Notification(String dateTimeIN, String txnNo, String channel, String subject, String message, String sender, String senderLogo, String payLoad, String readStatus, String customerID) {
        DateTimeIN = dateTimeIN;
        TxnNo = txnNo;
        Channel = channel;
        Subject = subject;
        Message = message;
        Sender = sender;
        SenderLogo = senderLogo;
        PayLoad = payLoad;
        ReadStatus = readStatus;
        CustomerID = customerID;
    }

    protected Notification(Parcel in) {
        DateTimeIN = in.readString();
        TxnNo = in.readString();
        Channel = in.readString();
        Subject = in.readString();
        Message = in.readString();
        Sender = in.readString();
        SenderLogo = in.readString();
        PayLoad = in.readString();
        ReadStatus = in.readString();
        CustomerID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DateTimeIN);
        dest.writeString(TxnNo);
        dest.writeString(Channel);
        dest.writeString(Subject);
        dest.writeString(Message);
        dest.writeString(Sender);
        dest.writeString(SenderLogo);
        dest.writeString(PayLoad);
        dest.writeString(ReadStatus);
        dest.writeString(CustomerID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getDateTimeIN() {
        return DateTimeIN;
    }

    public String getTxnNo() {
        return TxnNo;
    }

    public String getChannel() {
        return Channel;
    }

    public String getSubject() {
        return Subject;
    }

    public String getMessage() {
        return Message;
    }

    public String getSender() {
        return Sender;
    }

    public String getSenderLogo() {
        return SenderLogo;
    }

    public String getPayLoad() {
        return PayLoad;
    }

    public String getReadStatus() {
        return ReadStatus;
    }

    public String getCustomerID() {
        return CustomerID;
    }
}
