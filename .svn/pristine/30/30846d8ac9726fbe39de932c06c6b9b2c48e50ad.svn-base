package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User-PC on 9/12/2017.
 */

public class OrderPayments {
    @SerializedName("PaymentTxnNo")
    @Expose
    private String PaymentTxnNo;
    @SerializedName("OrderTxnID")
    @Expose
    private String OrderTxnID;
    @SerializedName("PaymentOption")
    @Expose
    private String PaymentOption;
    @SerializedName("TotalItemAmount")
    @Expose
    private double TotalItemAmount;
    @SerializedName("TotalAmount")
    @Expose
    private double TotalAmount;
    @SerializedName("AmountPaid")
    @Expose
    private double AmountPaid;
    @SerializedName("DeliveryCharge")
    @Expose
    private double DeliveryCharge;
    @SerializedName("PaymentStatus")
    @Expose
    private String PaymentStatus;
    
    public OrderPayments(String paymentTxnNo, String orderTxnID, String paymentOption, double totalAmount, double amountPaid, double deliveryCharge, String paymentStatus) {
        PaymentTxnNo = paymentTxnNo;
        OrderTxnID = orderTxnID;
        PaymentOption = paymentOption;
        TotalAmount = totalAmount;
        AmountPaid = amountPaid;
        DeliveryCharge = deliveryCharge;
        PaymentStatus = paymentStatus;
    }

    public OrderPayments(String paymentTxnNo, String orderTxnID, String paymentOption, double totalItemAmount, double totalAmount, double amountPaid, double deliveryCharge, String paymentStatus) {
        PaymentTxnNo = paymentTxnNo;
        OrderTxnID = orderTxnID;
        PaymentOption = paymentOption;
        TotalItemAmount = totalItemAmount;
        TotalAmount = totalAmount;
        AmountPaid = amountPaid;
        DeliveryCharge = deliveryCharge;
        PaymentStatus = paymentStatus;
    }

    public double getTotalItemAmount() {
        return TotalItemAmount;
    }

    public String getPaymentTxnNo() {
        return PaymentTxnNo;
    }

    public String getOrderTxnID() {
        return OrderTxnID;
    }

    public String getPaymentOption() {
        return PaymentOption;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public double getAmountPaid() {
        return AmountPaid;
    }

    public double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }
}
