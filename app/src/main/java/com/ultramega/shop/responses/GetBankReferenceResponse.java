package com.ultramega.shop.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.pojo.BankReference;

import java.util.ArrayList;
import java.util.List;


public class GetBankReferenceResponse {
    @SerializedName("data")
    @Expose
    private List<BankReference> bankReference = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<BankReference> getBankReference() {
        return bankReference;
    }

    public void setBankReference(List<BankReference> bankReference) {
        this.bankReference = bankReference;
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
