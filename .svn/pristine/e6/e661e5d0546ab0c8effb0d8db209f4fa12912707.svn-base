package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ShoppingCartsQueue;

import java.util.ArrayList;
import java.util.List;

public class AddUdateShoppingCartResponse {
    @SerializedName("data")
    @Expose
    private List<ShoppingCartsQueue> shoppingCartsQueues = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ShoppingCartsQueue> getShoppingCartsQueues() {
        return shoppingCartsQueues;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
