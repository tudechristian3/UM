package com.paymaya.sdk.android.checkout.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class OrdersQueue (

        @SerialName("ID") val ID :  String? = null,
        @SerialName("DateTimeIN") val DateTimeIN :  String? = null,
        @SerialName("OrderTxnID") val OrderTxnID :  String? = null,
        @SerialName("OrderDateTime") val OrderDateTime :  String? = null,
        @SerialName("CompletedDateTime") val CompletedDateTime :  String? = null,
        @SerialName("OrderType")  val OrderType :  String? = null,
        @SerialName("CustomerType")  val CustomerType :  String? = null,
        @SerialName("CustomerID")  val CustomerID :  String? = null,
        @SerialName("CustomerName")  val CustomerName  :  String? = null,
        @SerialName("CustomerMobileNumber")  val CustomerMobileNumber  :  String? = null,
        @SerialName("CustomerEmailAddress")  val CustomerEmailAddress  :  String? = null,
        @SerialName("CustomerDeliveryAddress")  val CustomerDeliveryAddress  :  String? = null,
        @SerialName("Longitude")  val Longitude  :  String? = null,
        @SerialName("Latitude")  val Latitude  :  String? = null,
        @SerialName("AccountType")  val AccountType  :  String? = null,
        @SerialName("AccountID")  val AccountID  :  String? = null,
        @SerialName("AccountUserID")  val AccountUserID  :  String? = null,
        @SerialName("TotalItems")  val TotalItems :  String? = null,
        @SerialName("TotalItemAmount")  val TotalItemAmount :  String? = null,
        @SerialName("DeliveryCharge")  val DeliveryCharge :  String? = null,
        @SerialName("TotalAmount")  val TotalAmount :  String? = null,
        @SerialName("OrderMedium")  val OrderMedium :  String? = null,
        @SerialName("NoOfRoute")  val NoOfRoute :  String? = null,
        @SerialName("RouteDateTime")  val RouteDateTime :  String? = null,
        @SerialName("ApprovedOrderDateTime")  val ApprovedOrderDateTime :  String? = null,
        @SerialName("PaymentTxnNo")  val PaymentTxnNo :  String? = null,
        @SerialName("PaymentDateTime")  val PaymentDateTime :  String? = null,
        @SerialName("ConfirmPaymentBy")  val ConfirmPaymentBy :  String? = null,
        @SerialName("ConfirmPaymentDateTime")  val ConfirmPaymentDateTime :  String? = null,
        @SerialName("OnDeliveryBy")  val OnDeliveryBy :  String? = null,
        @SerialName("OnDeliveryDateTime")  val OnDeliveryDateTime :  String? = null,
        @SerialName("isExpiredOrder")  val isExpiredOrder :  String? = null,
        @SerialName("ExpiredDateTime")  val ExpiredDateTime  :  String? = null,
        @SerialName("isReOpenOrder")  val isReOpenOrder  :  String? = null,
        @SerialName("ReOpenDateTime")  val ReOpenDateTime  :  String? = null,
        @SerialName("CompletedBy")  val CompletedBy  :  String? = null,
        @SerialName("Remarks")  val Remarks  :  String? = null,
        @SerialName("LastUpdateDateTime")  val LastUpdateDateTime :  String? = null,
        @SerialName("XMLDetails")  val XMLDetails  :  String? = null,
        @SerialName("CustomerRating")  val CustomerRating :  String? = null,
        @SerialName("CustomerRemarks")  val CustomerRemarks :  String? = null,
        @SerialName("isTaggedAsCompletedByConsumer")  val isTaggedAsCompletedByConsumer :  String? = null,
        @SerialName("isRegistered")  val isRegistered :  String? = null,
        @SerialName("isAssigned")  val isAssigned :  String? = null,
        @SerialName("AssignedBy")  val AssignedBy :  String? = null,
        @SerialName("Status")  val Status :  String? = null,
//OrderDetailsBinded to Order
        @SerialName("CatClass")  val CatClass :  String? = null,
        @SerialName("ItemCode")  val ItemCode :  String? = null,
        @SerialName("PackageCode")  val PackageCode :  String? = null,
        @SerialName("Price")  val Price :  String? = null,
        @SerialName("GrossPrice")  val GrossPrice :  String? = null,
        @SerialName("Quantity")  val Quantity :  String? = null,
        @SerialName("QuantityServed")  val QuantityServed :  String? = null,
        @SerialName("ItemPicUrl")  val ItemPicUrl :  String? = null,
        @SerialName("AddedDateTime")  val AddedDateTime :  String? = null,
//AdditionalCol or Return from Server
        @SerialName("TotalOrderAmount")  val TotalOrderAmount :  String? = null,
        @SerialName("TotalPackageAmount")  val TotalPackageAmount :  String? = null,
        @SerialName("PackSize")  val PackSize :  String? = null,
        @SerialName("PackageDescription") val PackageDescription :  String? = null
) : Parcelable
