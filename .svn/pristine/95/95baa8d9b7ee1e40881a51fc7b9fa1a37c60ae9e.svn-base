package com.ultramega.shop.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerCharge {
    @SerializedName("ChargeClass")
    @Expose
    private String ChargeClass;
    @SerializedName("CurrencyID")
    @Expose
    private String CurrencyID;
    @SerializedName("VariableFee")
    @Expose
    private double VariableFee;
    @SerializedName("BaseFee")
    @Expose
    private double BaseFee;
    @SerializedName("AmountFrom")
    @Expose
    private double AmountFrom;
    @SerializedName("AmountTo")
    @Expose
    private double AmountTo;

    public ConsumerCharge(String chargeClass, String currencyID, double variableFee, double baseFee, double amountFrom, double amountTo) {
        ChargeClass = chargeClass;
        CurrencyID = currencyID;
        VariableFee = variableFee;
        BaseFee = baseFee;
        AmountFrom = amountFrom;
        AmountTo = amountTo;
    }

    public String getChargeClass() {
        return ChargeClass;
    }

    public String getCurrencyID() {
        return CurrencyID;
    }

    public double getVariableFee() {
        return VariableFee;
    }

    public double getBaseFee() {
        return BaseFee;
    }

    public double getAmountFrom() {
        return AmountFrom;
    }

    public double getAmountTo() {
        return AmountTo;
    }
}
