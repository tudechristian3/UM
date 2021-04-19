package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.OrdersQueue;

import java.util.ArrayList;
import java.util.List;

public class GetOrdersQueueResponse {

    @SerializedName("data")
    @Expose
    private List<OrdersQueue> data;

    private List<OrdersQueue> ordersQueue = new ArrayList<>();
    @SerializedName("datahist")
    @Expose
    private List<OrdersQueue> ordersQueueHist = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("sessionid")
    @Expose
    private String session;

    public String getSession() {
        return session;
    }

    private List<OrdersQueue> getOrdersQueueHist() {
        return ordersQueueHist;
    }

    public List<OrdersQueue> getOrdersQueue() {
        ordersQueue.addAll(getOrdersQueueHist());
        return ordersQueue;
    }

    public void setOrdersQueue(List<OrdersQueue> ordersQueue) {
        this.ordersQueue = ordersQueue;
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

    public List<OrdersQueue> getData() {
        return data;
    }

    public void setData(List<OrdersQueue> data) {
        this.data = data;
    }
}
