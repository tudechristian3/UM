package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetS3keyResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetS3keyAPI {
    @FormUrlEncoded
    @POST("getS3key")
    Call<GetS3keyResponse> getS3keyCall(@Field("imei") String imei,
                                        @Field("userid") String userid,
                                        @Field("authcode") String authcode,
                                        @Field("sessionid") String sessionid);
}
