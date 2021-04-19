package com.ultramega.shop.services;


import com.ultramega.shop.responses.PartialRegisterWholeSalerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PartialRegisterWholeSalerAPI {
    @FormUrlEncoded
    @POST("partialRegisterWholeSaler")
    Call<PartialRegisterWholeSalerResponse> partialRegisterWholeSalerCall(@Field("mobilenumber") String mobilenumber,
                                                                          @Field("imei") String imei,
                                                                          @Field("sessionid") String sessionid,
                                                                          @Field("authcode") String authcode,
                                                                          @Field("processtype") String processtype);
}
