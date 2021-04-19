package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ConsumerProfile;

import java.util.List;


public class SetNewPasswordResponse {
    @SerializedName("data")
    @Expose
    private List<ConsumerProfile> consumerProfile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ConsumerProfile> getConsumerProfile() {
        return consumerProfile;
    }

    public void setConsumerProfile(List<ConsumerProfile> consumerProfile) {
        this.consumerProfile = consumerProfile;
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
