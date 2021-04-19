package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.WholesalerProfile;

import java.util.List;


public class LoginWholesalerResponse {
    @SerializedName("data")
    @Expose
    private List<WholesalerProfile> listprofile;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<WholesalerProfile> getListprofile() {
        return listprofile;
    }

    public void setListprofile(List<WholesalerProfile> listprofile) {
        this.listprofile = listprofile;
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
