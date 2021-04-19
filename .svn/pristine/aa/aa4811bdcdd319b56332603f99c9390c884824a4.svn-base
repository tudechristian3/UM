package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetCustomerMyListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetCustomerMyListAPI {
    @FormUrlEncoded
    @POST("getCustomerMyList")
    Call<GetCustomerMyListResponse> getCustomerMyListCall(@Field("imei") String imei,
                                                          @Field("authcode") String authcode,
                                                          @Field("sessionid") String sessionid,
                                                          @Field("customerid") String customerid,
                                                          @Field("usertype") String usertype,
                                                          @Field("userid") String userid,
                                                          @Field("limit") String limit);
}
