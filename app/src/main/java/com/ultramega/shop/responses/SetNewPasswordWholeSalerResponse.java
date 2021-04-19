package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.WholesalerProfile;

import java.util.List;


public class SetNewPasswordWholeSalerResponse {
    @SerializedName("data")
    @Expose
    private List<WholesalerProfile> wholesalerProfile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<WholesalerProfile> getWholesalerProfile() {
        return wholesalerProfile;
    }

    public void setWholesalerProfile(List<WholesalerProfile> wholesalerProfile) {
        this.wholesalerProfile = wholesalerProfile;
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
