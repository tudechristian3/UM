package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("delivery_fee_amount")
    @Expose
    String delivery_fee_amount;

    public String getDelivery_fee_amount() {
        return delivery_fee_amount;
    }

    @SerializedName("parameter_warnings")
    @Expose
    String parameter_warnings;

    public String getParameter_warnings() {
        return parameter_warnings;
    }

}
