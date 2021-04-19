package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetOrdersQueueResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetOrdersQueueAPI {
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
}
