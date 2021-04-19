package com.ultramega.shop.services;

import com.ultramega.shop.responses.ViewPaymentWalletReloadResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 04/09/2017.
 */

public interface ViewPaymentWalletAPI {
    @FormUrlEncoded
    @POST("getWalletReloadHistory")
    Call<ViewPaymentWalletReloadResponse> getWalletReloadHistoryCall(@Field("imei") String imei,
                                                                     @Field("authcode") String authcode,
                                                                     @Field("sessionid") String sessionid,
                                                                     @Field("consumerid") String consumerid,
                                                                     @Field("limit") String limit,
                                                                     @Field("userid") String userid,
                                                                     @Field("year") int year,
                                                                     @Field("month") int month);

}
