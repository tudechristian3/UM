package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.SpeedyData;

public class CalculateOrderChargeResponse {
    @SerializedName("sessionid")
    @Expose
    private String session;
    @SerializedName("data")
    @Expose
    private SpeedyData data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public SpeedyData getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
