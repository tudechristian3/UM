package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.OrderPayments;

import java.util.List;


public class GetPaymentsResponse {
    @SerializedName("data")
    @Expose
    private List<OrderPayments> orderPayments;
    @SerializedName("hist")
    @Expose
    private List<OrderPayments> orderPaymentsHist;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<OrderPayments> getOrderPayments() {
        return orderPayments;
    }

    public List<OrderPayments> getOrderPaymentsHist() {
        return orderPaymentsHist;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
