package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.CategoryItems;

import java.util.ArrayList;
import java.util.List;

public class GetDailyFindsResponse {
    @SerializedName("data")
    @Expose
    private List<CategoryItems> categoryItems = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<CategoryItems> getCategoryItems() {
        return categoryItems;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
