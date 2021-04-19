package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 23/08/2017.
 */

public class DeleteMyListResponse {

    @SerializedName("data")
    @Expose
    private List<String> dataArr = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sessionid")
    @Expose
    private String session;

    public String getSession() {
        return session;
    }

    public List<String> getDataArr() {
        return dataArr;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
