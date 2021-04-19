package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdatePasswordWholeSalerResponse {
    @SerializedName("data")
    @Expose
    private String updatePassword;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getUpdatePassword() {
        return updatePassword;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
