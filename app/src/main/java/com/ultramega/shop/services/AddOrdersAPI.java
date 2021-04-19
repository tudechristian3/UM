package com.ultramega.shop.services;


import com.ultramega.shop.responses.AddOrdersResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddOrdersAPI {
    @FormUrlEncoded
    @POST("addOrders")
    Call<AddOrdersResponse> addOrdersCall(@Field("imei") String imei,
                                          @Field("authcode") String authcode,
                                          @Field("sessionid") String sessionid,
                                          @Field("userid") String userid,
                                          @Field("customerid") String customerid,
                                          @Field("usertype") String usertype,
                                          @Field("ordertype") String ordertype,
                                          @Field("customername") String customername,
                                          @Field("customermobile") String customermobile,
                                          @Field("customeremail") String customeremail,
                                          @Field("customerdeliveryaddress") String customerdeliveryaddress,
                                          @Field("longitude") String longitude,
                                          @Field("latitude") String latitude,
                                          @Field("delivereefee") String  delivereefee,
                                          @Field("vehicleid") String  vehicleid,
                                          @Field("orderskus") String orderskus,
                                          @Field("customerremarks") String customerremarks,
                                          @Field("pickupschedule") String pickupschedule,
                                          @Field("branchid") String branchid,
                                          @Field("paymenttype") String  paymenttype);


//    @FormUrlEncoded
//    @POST("addOrders")
//    Call<AddOrdersResponse> addOrdersDeliverCall(@Field("imei") String imei,
//                                          @Field("authcode") String authcode,
//                                          @Field("sessionid") String sessionid,
//                                          @Field("userid") String userid,
//                                          @Field("customerid") String customerid,
//                                          @Field("usertype") String usertype,
//                                          @Field("ordertype") String ordertype,
//                                          @Field("customername") String customername,
//                                          @Field("customermobile") String customermobile,
//                                          @Field("customeremail") String customeremail,
//                                          @Field("customerdeliveryaddress") String customerdeliveryaddress,
//                                          @Field("longitude") String longitude,
//                                          @Field("latitude") String latitude,
//                                          @Field("delivereefee") String  delivereefee,
//                                          @Field("vehicleid") String  vehicleid,
//                                          @Field("orderskus") String orderskus,
//                                          @Field("customerremarks") String customerremarks,
//                                          @Field("pickupschedule") String pickupschedule,
//                                          @Field("branchid") String branchid,
//                                          @Field("paymenttype") String  paymenttype);



}
