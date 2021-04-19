package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.CategoryItems;

import java.util.ArrayList;
import java.util.List;

public class GetPromoItemsResponse {
    @SerializedName("data")
    @Expose
    private List<CategoryItems> promosList = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<CategoryItems> getPromosList() {
        return promosList;
    }

    public void setPromosList(List<CategoryItems> promosList) {
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
