package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerWallet;

import java.util.ArrayList;
import java.util.List;


public class GetConsumerWalletResponse {
    @SerializedName("data")
    @Expose
    private List<ConsumerWallet> consumerWallet = new ArrayList<>();
    @SerializedName("deliverychargelist")
    @Expose
    private List<ConsumerCharge> consumerCharge = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ConsumerWallet> getConsumerWallet() {
        return consumerWallet;
    }

    public List<ConsumerCharge> getConsumerCharge() {
        return consumerCharge;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
