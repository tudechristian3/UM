package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.MyList;
import com.ultramega.shop.pojo.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 18/09/2017.
 */

public class GetNotificationResponse {
    @SerializedName("data")
    @Expose
    private List<Notification> data = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Notification> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
