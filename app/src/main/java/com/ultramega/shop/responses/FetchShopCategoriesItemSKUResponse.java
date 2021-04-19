package com.ultramega.shop.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.ItemSKU;

import java.util.ArrayList;
import java.util.List;

public class FetchShopCategoriesItemSKUResponse {
    @SerializedName("data")
    @Expose
    private List<ItemSKU> categoryItemSKU = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ItemSKU> getCategoryItemSKU() {
        return categoryItemSKU;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
