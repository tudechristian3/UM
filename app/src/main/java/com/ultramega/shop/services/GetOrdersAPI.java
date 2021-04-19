package com.ultramega.shop.services;


import com.ultramega.shop.responses.CalculateOrderChargeResponse;
import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetOrdersQueueResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetOrdersAPI {
    @FormUrlEncoded
    @POST("getOrdersQueue")
    Call<GetOrdersQueueResponse> getOrdersQueueCall(@Field("imei") String imei,
                                                    @Field("authcode") String authcode,
                                                    @Field("sessionid") String sessionid,
                                                    @Field("customerid") String customerid,
                                                    @Field("usertype") String usertype,
                                                    @Field("limit") String limit,
                                                    @Field("userid") String userid,
                                                    @Field("year") int year,
                                                    @Field("month") int month);

    @FormUrlEncoded
    @POST("getOrdersHistory")
    Call<GetOrdersQueueResponse> getOrdersHistoryCall(@Field("imei") String imei,
                                                      @Field("authcode") String authcode,
                                                      @Field("sessionid") String sessionid,
                                                      @Field("customerid") String customerid,
                                                      @Field("usertype") String usertype,
                                                      @Field("limit") String limit,
                                                      @Field("userid") String userid,
                                                      @Field("year") int year,
                                                      @Field("month") int month,
                                                      @Field("issearch") String issearch);

    @FormUrlEncoded
    @POST("getOrderDetailsQueue")
    Call<GetOrdersQueueResponse> getOrderDetailsQueue(@Field("imei") String imei,
                                                    @Field("userid")String userid,
                                                    @Field("authcode") String authcode,
                                                    @Field("sessionid") String sessionid,
                                                    @Field("customerid") String customerid,
                                                    @Field("ordertxnid") String ordertxnid,
                                                    @Field("usertype") String usertype);


    @FormUrlEncoded
    @POST("getOrderDetailsHistory")
    Call<GetOrdersQueueResponse> getOrderDetailsHistory(@Field("imei") String imei,
                                                      @Field("userid")String userid,
                                                      @Field("authcode") String authcode,
                                                      @Field("sessionid") String sessionid,
                                                      @Field("customerid") String customerid,
                                                      @Field("ordertxnid") String ordertxnid,
                                                        @Field("limit") String limit,
                                                        @Field("year") String year,
                                                        @Field("month") String month,
                                                        @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("calculateOrderCharge")
    Call<CalculateOrderChargeResponse> calculateOrderCharge(@Field("usertype") String usertype,
                                                            @Field("sessionid") String sessionid,
                                                            @Field("imei") String imei,
                                                            @Field("authcode") String authcode,
                                                            @Field("customerid") String customerid,
                                                            @Field("userid") String userid,
                                                            @Field("mobilenumber") String mobilenumber,
                                                            @Field("vehicleid") String vehicleid,
                                                            @Field("vehicletype") String vehicletype,
                                                            @Field("longitude") String longitude,
                                                            @Field("latitude") String latitude,
                                                            @Field("branchid") String branchid,
                                                            @Field("address") String address);

}
