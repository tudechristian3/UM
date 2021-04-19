package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.OrderPayments;

public class PayOrderResponse {
    @SerializedName("data")
    @Expose
    private OrderPayments orderPayments;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public OrderPayments getOrderPayments() {
        return orderPayments;
    }

    public void setOrderPayments(OrderPayments orderPayments) {
        this.orderPayments = orderPayments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
