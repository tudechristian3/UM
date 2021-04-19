package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.PopularSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ban_Lenovo on 9/15/2017.
 */

public class GetPopularSearchesResponse {

    @SerializedName("data")
    @Expose
    private List<PopularSearch> popularSearches = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<PopularSearch> getPopularSearches() {
        return popularSearches;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
