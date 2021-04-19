package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.AmazonCredentials;


public class GetS3keyResponse {
    @SerializedName("data")
    @Expose
    private AmazonCredentials amazonCredentials;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public AmazonCredentials getAmazonCredentials() {
        return amazonCredentials;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
