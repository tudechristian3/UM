package com.ultramega.shop.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingCartsQueue {
    @SerializedName("DateTimeAdded")
    @Expose
    private String DateTimeAdded;
    @SerializedName("CustomerType")
    @Expose
    private String CustomerType;
    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;
    @SerializedName("IMEI")
    @Expose
    private String IMEI;
    @SerializedName("CatClass")
    @Expose
    private String CatClass;
    @SerializedName("ItemCode")
    @Expose
    private String ItemCode;
    @SerializedName("PackageCode")
    @Expose
    private String PackageCode;
    @SerializedName("Price")
    @Expose
    private double Price;
    @SerializedName("GrossPrice")
    @Expose
    private double GrossPrice;
    @SerializedName("Quantity")
    @Expose
    private int Quantity;
    @SerializedName("MinimumOrderQTY")
    @Expose
    private int MinimumOrderQTY;
    @SerializedName("IncrementalOrderQTY")
    @Expose
    private int IncrementalOrderQTY;
    @SerializedName("TotalAmount")
    @Expose
    private double TotalAmount;
    @SerializedName("ItemPicUrl")
    @Expose
    private String ItemPicUrl;
    @SerializedName("Remarks")
    @Expose
    private String Remarks;
    @SerializedName("LastUpdateDateTime")
    @Expose
    private String LastUpdateDateTime;
    @SerializedName("isRegistered")
    @Expose
    private int isRegistered;
    @SerializedName("OrderTxnID")
    @Expose
    private String OrderTxnID;
    @SerializedName("OrderDateTime")
    @Expose
    private String OrderDateTime;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("PricingGroup")
    @Expose
    private String PricingGroup;
    @SerializedName("ItemDescription")
    @Expose
    private String ItemDescription;
    @SerializedName("PackageDescription")
    @Expose
    private String PackageDescription;
    @SerializedName("PackSize")
    @Expose
    private String PackSize;
    @SerializedName("isBulk")
    @Expose
    private int isBulk;

    public ShoppingCartsQueue(String dateTimeAdded, String customerType, String customerID, String IMEI, String catClass, String itemCode, String packageCode, double price, double grossPrice, int quantity, int minimumOrderQTY, int incrementalOrderQTY, double totalAmount, String itemPicUrl, String remarks, String lastUpdateDateTime, int isRegistered, String orderTxnID, String orderDateTime, String status, String pricingGroup, String itemDescription, String packageDescription, String packSize, int isBulk) {
        DateTimeAdded = dateTimeAdded;
        CustomerType = customerType;
        CustomerID = customerID;
        this.IMEI = IMEI;
        CatClass = catClass;
        ItemCode = itemCode;
        PackageCode = packageCode;
        Price = price;
        GrossPrice = grossPrice;
        Quantity = quantity;
        MinimumOrderQTY = minimumOrderQTY;
        IncrementalOrderQTY = incrementalOrderQTY;
        TotalAmount = totalAmount;
        ItemPicUrl = itemPicUrl;
        Remarks = remarks;
        LastUpdateDateTime = lastUpdateDateTime;
        this.isRegistered = isRegistered;
        OrderTxnID = orderTxnID;
        OrderDateTime = orderDateTime;
        Status = status;
        PricingGroup = pricingGroup;
        ItemDescription = itemDescription;
        PackageDescription = packageDescription;
        PackSize = packSize;
        this.isBulk = isBulk;
    }

    public String getDateTimeAdded() {
        return DateTimeAdded;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getIMEI() {
        return IMEI;
    }

    public String getCatClass() {
        return CatClass;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getPackageCode() {
        return PackageCode;
    }

    public double getPrice() {
        return Price;
    }

    public double getGrossPrice() {
        return GrossPrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getMinimumOrderQTY() {
        return MinimumOrderQTY;
    }

    public int getIncrementalOrderQTY() {
        return IncrementalOrderQTY;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public String getItemPicUrl() {
        return ItemPicUrl;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getLastUpdateDateTime() {
        return LastUpdateDateTime;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public String getOrderTxnID() {
        return OrderTxnID;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public String getStatus() {
        return Status;
    }

    public String getPricingGroup() {
        return PricingGroup;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getPackageDescription() {
        return PackageDescription;
    }

    public String getPackSize() {
        return PackSize;
    }

    public int getIsBulk() {
        return isBulk;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
