package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.Branches;

import java.util.ArrayList;
import java.util.List;


public class GetBranchesResponse {
    @SerializedName("data")
    @Expose
    private List<Branches> getBranches = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Branches> getGetBranches() {
        return getBranches;
    }

    public void setGetBranches(List<Branches> getBranches) {
        this.getBranches = getBranches;
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
