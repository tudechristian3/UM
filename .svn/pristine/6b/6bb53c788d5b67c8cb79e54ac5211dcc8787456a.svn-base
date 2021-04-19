package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetConsumerWalletResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetConsumerWalletAPI {
    @FormUrlEncoded
    @POST("getConsumerWallet")
    Call<GetConsumerWalletResponse> getConsumerWalletCall(@Field("consumerid") String consumerid,
                                                           @Field("userid") String userid,
                                                           @Field("authcode") String authcode,
                                                           @Field("imei") String imei,
                                                           @Field("sessionid") String sessionid);
}
