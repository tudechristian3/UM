package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeedyData {

    @SerializedName("order")
    @Expose
    Order order;

    public Order getOrder() {
        return order;
    }

    @SerializedName("parameter_warnings")
    @Expose
    Order parameter_warnings;

    public Order getParameter_warnings() {
        return parameter_warnings;
    }
}

