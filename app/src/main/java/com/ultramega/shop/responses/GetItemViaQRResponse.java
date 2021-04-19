package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.scan.ScannedItem;

import java.util.ArrayList;
import java.util.List;

public class GetItemViaQRResponse {
    @SerializedName("data")
    @Expose
    private List<ScannedItem> getItemsViaQR = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ScannedItem> getGetItemsViaQR() {
        return getItemsViaQR;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
