package com.ultramega.shop.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingCartSummary {
    @SerializedName("TotalBulk")
    @Expose
    private int TotalBulk;
    @SerializedName("TotalNonBulk")
    @Expose
    private int TotalNonBulk;
    @SerializedName("Quantity")
    @Expose
    private int Quantity;
    @SerializedName("TotalAmount")
    @Expose
    private double TotalAmount;

    public ShoppingCartSummary(int quantity, double totalAmount) {
        Quantity = quantity;
        TotalAmount = totalAmount;
    }

    public ShoppingCartSummary(int totalBulk, int totalNonBulk, double totalAmount) {
        TotalBulk = totalBulk;
        TotalNonBulk = totalNonBulk;
        TotalAmount = totalAmount;
    }

    public int getTotalBulk() {
        return TotalBulk;
    }

    public int getTotalNonBulk() {
        return TotalNonBulk;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }
}
