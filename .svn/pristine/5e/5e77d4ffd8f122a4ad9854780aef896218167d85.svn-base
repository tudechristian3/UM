package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.WalletReload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/09/2017.
 */

public class ViewPaymentWalletReloadResponse {
    @SerializedName("data")
    @Expose
    private List<WalletReload> dataArr = new ArrayList<>();
    @SerializedName("hist")
    @Expose
    private List<WalletReload> histArr = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<WalletReload> getDataArr() {
        return dataArr;
    }

    public List<WalletReload> getHistArr() {
        return histArr;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
