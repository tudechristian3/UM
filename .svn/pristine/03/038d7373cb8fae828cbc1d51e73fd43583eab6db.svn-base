package com.ultramega.shop.services;

import com.ultramega.shop.responses.GetNotificationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 15/09/2017.
 */

public interface GetNotificationsAPI {
    @FormUrlEncoded
    @POST("getNotifications")
    Call<GetNotificationResponse> getNotificationsCall(@Field("imei") String imei,
                                                       @Field("authcode") String authcode,
                                                       @Field("sessionid") String sessionid,
                                                       @Field("usertype") String usertype,
                                                       @Field("customerid") String customerid,
                                                       @Field("userid") String userid,
                                                       @Field("limit") String limit,
                                                       @Field("year") String year,
                                                       @Field("month") String month);
}
