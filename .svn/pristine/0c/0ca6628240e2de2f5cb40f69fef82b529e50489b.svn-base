package com.ultramega.shop.services;


import com.ultramega.shop.responses.LoginResponse;
import com.ultramega.shop.responses.LoginWholesalerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginAPI {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginCall(@Field("userid") String userid,
                                  @Field("authcode") String authcode,
                                  @Field("imei") String imei,
                                  @Field("sessionid") String sessionid,
                                  @Field("password") String password,
                                  @Field("shoppingcarts") String shoppingcarts,
                                  @Field("token") String token);

    @FormUrlEncoded
    @POST("loginWholeSaler")
    Call<LoginWholesalerResponse> loginWholeSalerCall(@Field("userid") String userid,
                                                      @Field("authcode") String authcode,
                                                      @Field("imei") String imei,
                                                      @Field("sessionid") String sessionid,
                                                      @Field("password") String password,
                                                      @Field("token") String token);
}
