package com.ultramega.shop.services;


import com.ultramega.shop.responses.RegisterConsumerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterConsumerAPI {
    @FormUrlEncoded
    @POST("registerConsumer")
    Call<RegisterConsumerResponse> registerConsumerCall(@Field("mobilenumber") String mobilenumber,
                                                        @Field("authcode") String authcode,
                                                        @Field("imei") String imei,
                                                        @Field("firstname") String firstname,
                                                        @Field("lastname") String lastname,
                                                        @Field("password") String password,
                                                        @Field("countrycode") String countrycode,
                                                        @Field("country") String country,
                                                        @Field("profilepic") String profilepic,
                                                        @Field("sessionid") String sessionid,
                                                        @Field("interest") String interest,
                                                        @Field("facebookid") String facebookid,
                                                        @Field("otherinterest") String otherinterest,
                                                        @Field("shoppingcarts") String shoppingcarts,
                                                        @Field("token") String token);
}
