package com.ultramega.shop.services;


import com.ultramega.shop.responses.PayOrderResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PayOrderAPI {
    @FormUrlEncoded
    @POST("payOrder")
    Call<PayOrderResponse> payOrderCall(@Field("imei") String imei,
                                        @Field("authcode") String authcode,
                                        @Field("sessionid") String sessionid,
                                        @Field("customerid") String customerid,
                                        @Field("usertype") String usertype,
                                        @Field("ordertxnid") String ordertxnid,
                                        @Field("userid") String userid,
                                        @Field("customername") String customername);
}
