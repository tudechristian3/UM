package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ReloadConsumerWalletResponse {
    @SerializedName("data")
    @Expose
    private List<String> listwallet;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<String> getListwallet() {
        return listwallet;
    }

    public void setListwallet(List<String> listwallet) {
        this.listwallet = listwallet;
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
