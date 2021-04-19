package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by User on 22/09/2017.
 */

public class PushNotification implements Serializable {
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("senderlogo")
    @Expose
    private String senderlogo;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("txnno")
    @Expose
    private String txnno;
    @SerializedName("messageimg")
    @Expose
    private String messageimg;

    public String getSubject() {
        return subject;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderlogo() {
        return senderlogo;
    }

    public String getMessage() {
        return message;
    }

    public String getTxnno() {
        return txnno;
    }

    public String getMessageimg() {
        return messageimg;
    }
}
