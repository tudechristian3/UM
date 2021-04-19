package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.PromoPts;

import java.util.ArrayList;
import java.util.List;

public class GetPromoPointsResponse {
    @SerializedName("data")
    @Expose
    private List<PromoPts> promoPoints = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<PromoPts> getPromoPoints() {
        return promoPoints;
    }

    public void setPromoPoints(List<PromoPts> promoPoints) {
        this.promoPoints = promoPoints;
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
