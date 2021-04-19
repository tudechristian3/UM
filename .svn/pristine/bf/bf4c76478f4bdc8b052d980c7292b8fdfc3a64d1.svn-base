package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.SupplierItems;

import java.util.ArrayList;
import java.util.List;

public class GetSupplierItemsResponse {
    @SerializedName("data")
    @Expose
    private List<SupplierItems> supplieritems = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<SupplierItems> getSupplieritems() {
        return supplieritems;
    }

    public void setSupplieritems(List<SupplierItems> supplieritems) {
        this.supplieritems = supplieritems;
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
