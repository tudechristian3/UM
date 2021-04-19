package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.Promos;

import java.util.ArrayList;
import java.util.List;

public class GetPromosResponse {
    @SerializedName("data")
    @Expose
    private List<Promos> promosList = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Promos> getPromosList() {
        return promosList;
    }

    public void setPromosList(List<Promos> promosList) {
        this.promosList = promosList;
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
