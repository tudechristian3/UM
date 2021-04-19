package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ConsumerProfile;

import java.util.ArrayList;
import java.util.List;


public class GetAccountInformationResponse {
    @SerializedName("data")
    @Expose
    private List<ConsumerProfile> listProfile = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ConsumerProfile> getListProfile() {
        return listProfile;
    }

    public void setListProfile(List<ConsumerProfile> listProfile) {
        this.listProfile = listProfile;
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
