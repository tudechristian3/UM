package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetAccountInformationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetAccountInformationAPI {
    @FormUrlEncoded
    @POST("getAccountInformation")
    Call<GetAccountInformationResponse> getAccountInformationCall(@Field("consumerid") String consumerid,
                                                                  @Field("userid") String userid,
                                                                  @Field("authcode") String authcode,
                                                                  @Field("imei") String imei,
                                                                  @Field("sessionid") String sessionid);
}
