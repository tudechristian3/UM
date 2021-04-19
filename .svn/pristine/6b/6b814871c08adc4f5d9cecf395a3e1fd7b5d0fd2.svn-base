package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSKU implements Parcelable {
    @SerializedName("ItemCode")
    @Expose
    private String ItemCode;
    @SerializedName("ItemDescription")
    @Expose
    private String ItemDescription;
    @SerializedName("PackSize")
    @Expose
    private String PackSize;
    @SerializedName("Category")
    @Expose
    private String Category;
    @SerializedName("CatClass")
    @Expose
    private String CatClass;
    @SerializedName("SupplierID")
    @Expose
    private String SupplierID;
    @SerializedName("PackageCode")
    @Expose
    private String PackageCode;
    @SerializedName("Price")
    @Expose
    private double Price;
    @SerializedName("GrossPrice")
    @Expose
    private double GrossPrice;
    @SerializedName("isPromo")
    @Expose
    private int isPromo;
    @SerializedName("PromoDetails")
    @Expose
    private String PromoDetails;
    @SerializedName("MinimumOrderQTY")
    @Expose
    private int MinimumOrderQTY;
    @SerializedName("IncrementalOrderQTY")
    @Expose
    private int IncrementalOrderQTY;
    @SerializedName("Barcode")
    @Expose
    private String Barcode;
    @SerializedName("PackageDescription")
    @Expose
    private String PackageDescription;
    @SerializedName("ItemPicURL")
    @Expose
    private String ItemPicURL;
    @SerializedName("isBulk")
    @Expose
    private int isBulk;

    public ItemSKU(String itemCode, String itemDescription, String packSize, String category, String catClass, String supplierID, String packageCode, double price, double grossPrice, int isPromo, String promoDetails, int minimumOrderQTY, int incrementalOrderQTY, String barcode, String packageDescription, String itemPicURL, int isBulk) {
        ItemCode = itemCode;
        ItemDescription = itemDescription;
        PackSize = packSize;
        Category = category;
        CatClass = catClass;
        SupplierID = supplierID;
        PackageCode = packageCode;
        Price = price;
        GrossPrice = grossPrice;
        this.isPromo = isPromo;
        PromoDetails = promoDetails;
        MinimumOrderQTY = minimumOrderQTY;
        IncrementalOrderQTY = incrementalOrderQTY;
        Barcode = barcode;
        PackageDescription = packageDescription;
        ItemPicURL = itemPicURL;
        this.isBulk = isBulk;
    }

    public ItemSKU(String itemCode, String catClass, String packageDescription) {
        ItemCode = itemCode;
        CatClass = catClass;
        PackageDescription = packageDescription;
    }

    protected ItemSKU(Parcel in) {
        ItemCode = in.readString();
        ItemDescription = in.readString();
        PackSize = in.readString();
        Category = in.readString();
        CatClass = in.readString();
        SupplierID = in.readString();
        PackageCode = in.readString();
        Price = in.readDouble();
        GrossPrice = in.readDouble();
        isPromo = in.readInt();
        PromoDetails = in.readString();
        MinimumOrderQTY = in.readInt();
        IncrementalOrderQTY = in.readInt();
        Barcode = in.readString();
        PackageDescription = in.readString();
        ItemPicURL = in.readString();
        isBulk = in.readInt();
    }

    public static final Creator<ItemSKU> CREATOR = new Creator<ItemSKU>() {
        @Override
        public ItemSKU createFromParcel(Parcel in) {
            return new ItemSKU(in);
        }

        @Override
        public ItemSKU[] newArray(int size) {
            return new ItemSKU[size];
        }
    };

    public String getItemCode() {
        return ItemCode;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getPackSize() {
        return PackSize;
    }

    public String getCategory() {
        return Category;
    }

    public String getCatClass() {
        return CatClass;
    }

    public String getSupplierID() {
        return SupplierID;
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

    public int getIsPromo() {
        return isPromo;
    }

    public String getPromoDetails() {
        return PromoDetails;
    }

    public int getMinimumOrderQTY() {
        return MinimumOrderQTY;
    }

    public int getIncrementalOrderQTY() {
        return IncrementalOrderQTY;
    }

    public String getBarcode() {
        return Barcode;
    }

    public String getPackageDescription() {
        return PackageDescription;
    }

    public String getItemPicURL() {
        return ItemPicURL;
    }

    public int getIsBulk() {
        return isBulk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ItemCode);
        parcel.writeString(ItemDescription);
        parcel.writeString(PackSize);
        parcel.writeString(Category);
        parcel.writeString(CatClass);
        parcel.writeString(SupplierID);
        parcel.writeString(PackageCode);
        parcel.writeDouble(Price);
        parcel.writeDouble(GrossPrice);
        parcel.writeInt(isPromo);
        parcel.writeString(PromoDetails);
        parcel.writeInt(MinimumOrderQTY);
        parcel.writeInt(IncrementalOrderQTY);
        parcel.writeString(Barcode);
        parcel.writeString(PackageDescription);
        parcel.writeString(ItemPicURL);
        parcel.writeInt(isBulk);
    }
}
