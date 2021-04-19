package com.ultramega.shop.services;


import com.ultramega.shop.responses.AddConsumerAddressResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddConsumerAddressAPI {
    @FormUrlEncoded
    @POST("addConsumerAddress")
    Call<AddConsumerAddressResponse> addConsumerAddressCall(@Field("consumerid") String consumerid,
                                                            @Field("userid") String userid,
                                                            @Field("authcode") String authcode,
                                                            @Field("imei") String imei,
                                                            @Field("sessionid") String sessionid,
                                                            @Field("streetaddress") String streetaddress,
                                                            @Field("city") String city,
                                                            @Field("province") String province,
                                                            @Field("zip") String zip,
                                                            @Field("country") String country,
                                                            @Field("isdefault") String isdefault,
                                                            @Field("longitude") String longitude,
                                                            @Field("latitude") String latitude);
}
