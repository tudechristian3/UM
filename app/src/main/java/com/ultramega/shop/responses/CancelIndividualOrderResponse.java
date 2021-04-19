package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CancelIndividualOrderResponse {
    @SerializedName("data")
    @Expose
    private List shoppingCartsQueues = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List getShoppingCartsQueues() {
        return shoppingCartsQueues;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
