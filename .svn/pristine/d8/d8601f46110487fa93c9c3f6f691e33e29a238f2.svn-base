package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetPaymentsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetPaymentsResponseAPI {
    @FormUrlEncoded
    @POST("getPayments")
    Call<GetPaymentsResponse> getPaymentsCall(@Field("imei") String imei,
                                              @Field("authcode") String authcode,
                                              @Field("sessionid") String sessionid,
                                              @Field("customerid") String customerid,
                                              @Field("usertype") String usertype,
                                              @Field("userid") String userid,
                                              @Field("year") String year,
                                              @Field("month") String month);
}
