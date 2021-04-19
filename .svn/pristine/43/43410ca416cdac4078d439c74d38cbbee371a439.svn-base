package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class PartialRegistrationEnterAccessCodeResponse {
    @SerializedName("data")
    @Expose
    private List AuthenticationCode = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List getAuthenticationCode() {
        return AuthenticationCode;
    }

    public void setAuthenticationCode(List authenticationCode) {
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
