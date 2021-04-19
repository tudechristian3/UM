package com.ultramega.shop.services;

import com.ultramega.shop.responses.GetNotificationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Tony on 12/10/2017.
 */

public interface UpdateNotificationStatusAPI {
    @FormUrlEncoded
    @POST("updateNotificationStatus")
    Call<GetNotificationResponse> updateNotificationStatusCall(@Field("imei") String imei,
                                                               @Field("userid") String userid,
                                                               @Field("authcode") String authcode,
                                                               @Field("sessionid") String sessionid,
                                                               @Field("customerid") String customerid,
                                                               @Field("year") String year,
                                                               @Field("month") String month,
                                                               @Field("txnno") String txnno);
}
