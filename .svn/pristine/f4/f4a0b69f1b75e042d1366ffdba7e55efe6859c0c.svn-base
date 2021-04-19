package com.ultramega.shop.services;

import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetItemPackagesResponse;
import com.ultramega.shop.responses.UpayResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpayAPI {

    @FormUrlEncoded
    @POST("sendUPayPaymentRequest")
    Call<UpayResponse> sendUPayPaymentRequest(@Field("imei") String imei,
                                              @Field("authcode") String authcode,
                                              @Field("sessionid") String sessionid,
                                              @Field("customerid") String customerid,
                                              @Field("usertype") String usertype,
                                              @Field("userid") String userid,
                                              @Field("ordertxnid") String ordertxnid,
                                              @Field("mobilenumber") String mobilenumber);

    @FormUrlEncoded
    @POST("confirmUPayPayment")
    Call<UpayResponse> confirmUPayPayment(@Field("imei") String imei,
                                                 @Field("authcode") String authcode,
                                                 @Field("sessionid") String sessionid,
                                                 @Field("customerid") String customerid,
                                                 @Field("usertype") String usertype,
                                                 @Field("userid") String userid,
                                                 @Field("ordertxnid") String ordertxnid,
                                                 @Field("mobilenumber") String mobilenumber,
                                                 @Field("otp") String otp);

}
