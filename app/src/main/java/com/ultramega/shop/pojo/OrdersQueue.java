package com.ultramega.shop.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ultramega.shop.activity.PaymentOptionActivity;

/**
 * Created by User-PC on 7/18/2017.
 */

public class OrdersQueue implements Parcelable {
    @SerializedName("ID")
    @Expose
    private int ID;
    @SerializedName("DateTimeIN")
    @Expose
    private String DateTimeIN;
    @SerializedName("OrderTxnID")
    @Expose
    private String OrderTxnID;
    @SerializedName("OrderDateTime")
    @Expose
    private String OrderDateTime;
    @SerializedName("CompletedDateTime")
    @Expose
    private String CompletedDateTime;
    @SerializedName("OrderType")
    @Expose
    private String OrderType;
    @SerializedName("CustomerType")
    @Expose
    private String CustomerType;
    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;
    @SerializedName("CustomerName")
    @Expose
    private String CustomerName;
    @SerializedName("CustomerMobileNumber")
    @Expose
    private String CustomerMobileNumber;
    @SerializedName("CustomerEmailAddress")
    @Expose
    private String CustomerEmailAddress;
    @SerializedName("CustomerDeliveryAddress")
    @Expose
    private String CustomerDeliveryAddress;
    @SerializedName("Longitude")
    @Expose
    private String Longitude;
    @SerializedName("Latitude")
    @Expose
    private String Latitude;
    @SerializedName("AccountType")
    @Expose
    private String AccountType;
    @SerializedName("AccountID")
    @Expose
    private String AccountID;
    @SerializedName("AccountUserID")
    @Expose
    private String AccountUserID;
    @SerializedName("TotalItems")
    @Expose
    private int TotalItems;
    @SerializedName("TotalItemAmount")
    @Expose
    private double TotalItemAmount;
    @SerializedName("DeliveryCharge")
    @Expose
    private double DeliveryCharge;
    @SerializedName("TotalAmount")
    @Expose
    private double TotalAmount;
    @SerializedName("OrderMedium")
    @Expose
    private String OrderMedium;
    @SerializedName("NoOfRoute")
    @Expose
    private int NoOfRoute;
    @SerializedName("RouteDateTime")
    @Expose
    private String RouteDateTime;
    @SerializedName("ApprovedOrderDateTime")
    @Expose
    private String ApprovedOrderDateTime;
    @SerializedName("PaymentTxnNo")
    @Expose
    private String PaymentTxnNo;
    @SerializedName("PaymentDateTime")
    @Expose
    private String PaymentDateTime;
    @SerializedName("ConfirmPaymentBy")
    @Expose
    private String ConfirmPaymentBy;
    @SerializedName("ConfirmPaymentDateTime")
    @Expose
    private String ConfirmPaymentDateTime;
    @SerializedName("OnDeliveryBy")
    @Expose
    private String OnDeliveryBy;
    @SerializedName("OnDeliveryDateTime")
    @Expose
    private String OnDeliveryDateTime;
    @SerializedName("isExpiredOrder")
    @Expose
    private int isExpiredOrder;
    @SerializedName("ExpiredDateTime")
    @Expose
    private String ExpiredDateTime;
    @SerializedName("isReOpenOrder")
    @Expose
    private int isReOpenOrder;
    @SerializedName("ReOpenDateTime")
    @Expose
    private String ReOpenDateTime;
    @SerializedName("CompletedBy")
    @Expose
    private String CompletedBy;
    @SerializedName("Remarks")
    @Expose
    private String Remarks;
    @SerializedName("LastUpdateDateTime")
    @Expose
    private String LastUpdateDateTime;
    @SerializedName("XMLDetails")
    @Expose
    private String XMLDetails;
    @SerializedName("CustomerRating")
    @Expose
    private String CustomerRating;
    @SerializedName("CustomerRemarks")
    @Expose
    private String CustomerRemarks;
    @SerializedName("isTaggedAsCompletedByCustomer")
    @Expose
    private int isTaggedAsCompletedByConsumer;
    @SerializedName("isRegistered")
    @Expose
    private int isRegistered;
    @SerializedName("isAssigned")
    @Expose
    private int isAssigned;
    @SerializedName("AssignedBy")
    @Expose
    private String AssignedBy;
    @SerializedName("Status")
    @Expose
    private String Status;
    //OrderDetailsBinded to Order
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
    @SerializedName("QuantityServed")
    @Expose
    private int QuantityServed;
    @SerializedName("ItemPicUrl")
    @Expose
    private String ItemPicUrl;
    @SerializedName("AddedDateTime")
    @Expose
    private String AddedDateTime;
    //AdditionalCol or Return from Server
    @SerializedName("TotalOrderAmount")
    @Expose
    private double TotalOrderAmount;
    @SerializedName("TotalPackageAmount")
    @Expose
    private double TotalPackageAmount;
    @SerializedName("ItemDescription")
    @Expose
    private String ItemDescription;
    @SerializedName("PackSize")
    @Expose
    private String PackSize;
    @SerializedName("PackageDescription")
    @Expose
    private String PackageDescription;

    @SerializedName("PaymentType")
    @Expose
    private String PaymentType;



    public OrdersQueue(int ID, String dateTimeIN, String orderTxnID, String orderDateTime, String completedDateTime, String orderType, String customerType, String customerID, String customerName, String customerMobileNumber, String customerEmailAddress, String customerDeliveryAddress, String longitude, String latitude, String accountType, String accountID, String accountUserID, int totalItems, double totalItemAmount, double deliveryCharge, double totalAmount, String orderMedium, int noOfRoute, String routeDateTime, String approvedOrderDateTime, String paymentTxnNo, String paymentDateTime, String confirmPaymentBy, String confirmPaymentDateTime, String onDeliveryBy, String onDeliveryDateTime, int isExpiredOrder, String expiredDateTime, int isReOpenOrder, String reOpenDateTime, String completedBy, String remarks, String lastUpdateDateTime, String XMLDetails, String customerRating, String customerRemarks, int isTaggedAsCompletedByConsumer, int isRegistered, int isAssigned, String assignedBy, String status, String catClass, String itemCode, String packageCode, double price, double grossPrice, int quantity, int quantityServed, String itemPicUrl, String addedDateTime, double totalOrderAmount, double totalPackageAmount, String itemDescription, String packSize, String packageDescription,String paymentOption) {
        this.ID = ID;
        DateTimeIN = dateTimeIN;
        OrderTxnID = orderTxnID;
        OrderDateTime = orderDateTime;
        CompletedDateTime = completedDateTime;
        OrderType = orderType;
        CustomerType = customerType;
        CustomerID = customerID;
        CustomerName = customerName;
        CustomerMobileNumber = customerMobileNumber;
        CustomerEmailAddress = customerEmailAddress;
        CustomerDeliveryAddress = customerDeliveryAddress;
        Longitude = longitude;
        Latitude = latitude;
        AccountType = accountType;
        AccountID = accountID;
        AccountUserID = accountUserID;
        TotalItems = totalItems;
        TotalItemAmount = totalItemAmount;
        DeliveryCharge = deliveryCharge;
        TotalAmount = totalAmount;
        OrderMedium = orderMedium;
        NoOfRoute = noOfRoute;
        RouteDateTime = routeDateTime;
        ApprovedOrderDateTime = approvedOrderDateTime;
        PaymentTxnNo = paymentTxnNo;
        PaymentDateTime = paymentDateTime;
        ConfirmPaymentBy = confirmPaymentBy;
        ConfirmPaymentDateTime = confirmPaymentDateTime;
        OnDeliveryBy = onDeliveryBy;
        OnDeliveryDateTime = onDeliveryDateTime;
        this.isExpiredOrder = isExpiredOrder;
        ExpiredDateTime = expiredDateTime;
        this.isReOpenOrder = isReOpenOrder;
        ReOpenDateTime = reOpenDateTime;
        CompletedBy = completedBy;
        Remarks = remarks;
        LastUpdateDateTime = lastUpdateDateTime;
        this.XMLDetails = XMLDetails;
        CustomerRating = customerRating;
        CustomerRemarks = customerRemarks;
        this.isTaggedAsCompletedByConsumer = isTaggedAsCompletedByConsumer;
        this.isRegistered = isRegistered;
        this.isAssigned = isAssigned;
        AssignedBy = assignedBy;
        Status = status;
        CatClass = catClass;
        ItemCode = itemCode;
        PackageCode = packageCode;
        Price = price;
        GrossPrice = grossPrice;
        Quantity = quantity;
        QuantityServed = quantityServed;
        ItemPicUrl = itemPicUrl;
        AddedDateTime = addedDateTime;
        TotalOrderAmount = totalOrderAmount;
        TotalPackageAmount = totalPackageAmount;
        ItemDescription = itemDescription;
        PackSize = packSize;
        PackageDescription = packageDescription;
        PaymentType = paymentOption;
    }

    protected OrdersQueue(Parcel in) {
        ID = in.readInt();
        DateTimeIN = in.readString();
        OrderTxnID = in.readString();
        OrderDateTime = in.readString();
        CompletedDateTime = in.readString();
        OrderType = in.readString();
        CustomerType = in.readString();
        CustomerID = in.readString();
        CustomerName = in.readString();
        CustomerMobileNumber = in.readString();
        CustomerEmailAddress = in.readString();
        CustomerDeliveryAddress = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
        AccountType = in.readString();
        AccountID = in.readString();
        AccountUserID = in.readString();
        TotalItems = in.readInt();
        TotalItemAmount = in.readDouble();
        DeliveryCharge = in.readDouble();
        TotalAmount = in.readDouble();
        OrderMedium = in.readString();
        NoOfRoute = in.readInt();
        RouteDateTime = in.readString();
        ApprovedOrderDateTime = in.readString();
        PaymentTxnNo = in.readString();
        PaymentDateTime = in.readString();
        ConfirmPaymentBy = in.readString();
        ConfirmPaymentDateTime = in.readString();
        OnDeliveryBy = in.readString();
        OnDeliveryDateTime = in.readString();
        isExpiredOrder = in.readInt();
        ExpiredDateTime = in.readString();
        isReOpenOrder = in.readInt();
        ReOpenDateTime = in.readString();
        CompletedBy = in.readString();
        Remarks = in.readString();
        LastUpdateDateTime = in.readString();
        XMLDetails = in.readString();
        CustomerRating = in.readString();
        CustomerRemarks = in.readString();
        isTaggedAsCompletedByConsumer = in.readInt();
        isRegistered = in.readInt();
        isAssigned = in.readInt();
        AssignedBy = in.readString();
        Status = in.readString();
        CatClass = in.readString();
        ItemCode = in.readString();
        PackageCode = in.readString();
        Price = in.readDouble();
        GrossPrice = in.readDouble();
        Quantity = in.readInt();
        QuantityServed = in.readInt();
        ItemPicUrl = in.readString();
        AddedDateTime = in.readString();
        TotalOrderAmount = in.readDouble();
        TotalPackageAmount = in.readDouble();
        ItemDescription = in.readString();
        PackSize = in.readString();
        PackageDescription = in.readString();
        PaymentType = in.readString();
    }

    public static final Creator<OrdersQueue> CREATOR = new Creator<OrdersQueue>() {
        @Override
        public OrdersQueue createFromParcel(Parcel in) {
            return new OrdersQueue(in);
        }

        @Override
        public OrdersQueue[] newArray(int size) {
            return new OrdersQueue[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getDateTimeIN() {
        return DateTimeIN;
    }

    public String getOrderTxnID() {
        return OrderTxnID;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public String getCompletedDateTime() {
        return CompletedDateTime;
    }

    public String getOrderType() {
        return OrderType;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerMobileNumber() {
        return CustomerMobileNumber;
    }

    public String getCustomerEmailAddress() {
        return CustomerEmailAddress;
    }

    public String getCustomerDeliveryAddress() {
        return CustomerDeliveryAddress;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getAccountType() {
        return AccountType;
    }

    public String getAccountID() {
        return AccountID;
    }

    public String getAccountUserID() {
        return AccountUserID;
    }

    public int getTotalItems() {
        return TotalItems;
    }

    public double getTotalItemAmount() {
        return TotalItemAmount;
    }

    public double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public String getOrderMedium() {
        return OrderMedium;
    }

    public int getNoOfRoute() {
        return NoOfRoute;
    }

    public String getRouteDateTime() {
        return RouteDateTime;
    }

    public String getApprovedOrderDateTime() {
        return ApprovedOrderDateTime;
    }

    public String getPaymentTxnNo() {
        return PaymentTxnNo;
    }

    public String getPaymentDateTime() {
        return PaymentDateTime;
    }

    public String getConfirmPaymentBy() {
        return ConfirmPaymentBy;
    }

    public String getConfirmPaymentDateTime() {
        return ConfirmPaymentDateTime;
    }

    public String getOnDeliveryBy() {
        return OnDeliveryBy;
    }

    public String getOnDeliveryDateTime() {
        return OnDeliveryDateTime;
    }

    public int getIsExpiredOrder() {
        return isExpiredOrder;
    }

    public String getExpiredDateTime() {
        return ExpiredDateTime;
    }

    public int getIsReOpenOrder() {
        return isReOpenOrder;
    }

    public String getReOpenDateTime() {
        return ReOpenDateTime;
    }

    public String getCompletedBy() {
        return CompletedBy;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getLastUpdateDateTime() {
        return LastUpdateDateTime;
    }

    public String getXMLDetails() {
        return XMLDetails;
    }

    public String getCustomerRating() {
        return CustomerRating;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public int getIsTaggedAsCompletedByConsumer() {
        return isTaggedAsCompletedByConsumer;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public int getIsAssigned() {
        return isAssigned;
    }

    public String getAssignedBy() {
        return AssignedBy;
    }

    public String getStatus() {
        return Status;
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

    public int getQuantityServed() {
        return QuantityServed;
    }

    public String getItemPicUrl() {
        return ItemPicUrl;
    }

    public String getAddedDateTime() {
        return AddedDateTime;
    }

    public double getTotalOrderAmount() {
        return TotalOrderAmount;
    }

    public double getTotalPackageAmount() {
        return TotalPackageAmount;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public String getPackSize() {
        return PackSize;
    }

    public String getPackageDescription() {
        return PackageDescription;
    }

    public String getPaymentType() { return PaymentType; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(DateTimeIN);
        parcel.writeString(OrderTxnID);
        parcel.writeString(OrderDateTime);
        parcel.writeString(CompletedDateTime);
        parcel.writeString(OrderType);
        parcel.writeString(CustomerType);
        parcel.writeString(CustomerID);
        parcel.writeString(CustomerName);
        parcel.writeString(CustomerMobileNumber);
        parcel.writeString(CustomerEmailAddress);
        parcel.writeString(CustomerDeliveryAddress);
        parcel.writeString(Longitude);
        parcel.writeString(Latitude);
        parcel.writeString(AccountType);
        parcel.writeString(AccountID);
        parcel.writeString(AccountUserID);
        parcel.writeInt(TotalItems);
        parcel.writeDouble(TotalItemAmount);
        parcel.writeDouble(DeliveryCharge);
        parcel.writeDouble(TotalAmount);
        parcel.writeString(OrderMedium);
        parcel.writeInt(NoOfRoute);
        parcel.writeString(RouteDateTime);
        parcel.writeString(ApprovedOrderDateTime);
        parcel.writeString(PaymentTxnNo);
        parcel.writeString(PaymentDateTime);
        parcel.writeString(ConfirmPaymentBy);
        parcel.writeString(ConfirmPaymentDateTime);
        parcel.writeString(OnDeliveryBy);
        parcel.writeString(OnDeliveryDateTime);
        parcel.writeInt(isExpiredOrder);
        parcel.writeString(ExpiredDateTime);
        parcel.writeInt(isReOpenOrder);
        parcel.writeString(ReOpenDateTime);
        parcel.writeString(CompletedBy);
        parcel.writeString(Remarks);
        parcel.writeString(LastUpdateDateTime);
        parcel.writeString(XMLDetails);
        parcel.writeString(CustomerRating);
        parcel.writeString(CustomerRemarks);
        parcel.writeInt(isTaggedAsCompletedByConsumer);
        parcel.writeInt(isRegistered);
        parcel.writeInt(isAssigned);
        parcel.writeString(AssignedBy);
        parcel.writeString(Status);
        parcel.writeString(CatClass);
        parcel.writeString(ItemCode);
        parcel.writeString(PackageCode);
        parcel.writeDouble(Price);
        parcel.writeDouble(GrossPrice);
        parcel.writeInt(Quantity);
        parcel.writeInt(QuantityServed);
        parcel.writeString(ItemPicUrl);
        parcel.writeString(AddedDateTime);
        parcel.writeDouble(TotalOrderAmount);
        parcel.writeDouble(TotalPackageAmount);
        parcel.writeString(ItemDescription);
        parcel.writeString(PackSize);
        parcel.writeString(PackageDescription);
        parcel.writeString(PaymentType);
    }
}
