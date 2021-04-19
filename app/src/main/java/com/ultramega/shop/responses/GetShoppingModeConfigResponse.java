package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ShoppingModeConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User-PC on 7/1/2017.
 */

public class GetShoppingModeConfigResponse {
    @SerializedName("data")
    @Expose
    private List<ShoppingModeConfig> shoppingModeConfigList = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ShoppingModeConfig> getShoppingModeConfigList() {
        return shoppingModeConfigList;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
