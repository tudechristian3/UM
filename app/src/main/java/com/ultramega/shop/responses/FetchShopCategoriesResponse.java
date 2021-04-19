package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.Category;

import java.util.ArrayList;
import java.util.List;

public class FetchShopCategoriesResponse {
    @SerializedName("data")
    @Expose
    private List<Category> categories = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Category> getCategories() {
        return categories;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
