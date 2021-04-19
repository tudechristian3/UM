package com.ultramega.shop.services;


import com.ultramega.shop.responses.PartialRegistrationEnterAccessCodeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PartialRegistrationEnterAccessCodeAPI {
    @FormUrlEncoded
    @POST("partialRegistrationEnterAccessCode")
    Call<PartialRegistrationEnterAccessCodeResponse> partialRegistrationEnterAccessCodeCall(@Field("mobilenumber") String mobilenumber,
                                                                                            @Field("accesscode") String accesscode,
                                                                                            @Field("authcode") String authcode,
                                                                                            @Field("authenticationid") String authenticationid,
                                                                                            @Field("imei") String imei,
                                                                                            @Field("sessionid") String sessionid);

    @FormUrlEncoded
    @POST("partialRegistrationWholeSalerAccessCode")
    Call<PartialRegistrationEnterAccessCodeResponse> partialRegistrationWholeSalerAccessCodeCall(@Field("mobilenumber") String mobilenumber,
                                                                                            @Field("accesscode") String accesscode,
                                                                                            @Field("authcode") String authcode,
                                                                                            @Field("authenticationid") String authenticationid,
                                                                                            @Field("imei") String imei,
                                                                                            @Field("sessionid") String sessionid);
}
