package com.ultramega.shop.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Points {

    @SerializedName("different_regions")
    @Expose
    String different_regions;

    public String getDifferent_regions() {
        return different_regions;
    }
}
