package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PartialRegisterWholeSalerResponse {
    @SerializedName("data")
    @Expose
    private String AuthenticationCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getAuthenticationCode() {
        return AuthenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        AuthenticationCode = authenticationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
