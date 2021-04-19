package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetBankReferenceResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetBankReferenceAPI {
    @FormUrlEncoded
    @POST("getBankReference")
    Call<GetBankReferenceResponse> getBankReferenceCall(@Field("imei") String imei,
                                                        @Field("authcode") String authcode,
                                                        @Field("sessionid") String sessionid);
}
