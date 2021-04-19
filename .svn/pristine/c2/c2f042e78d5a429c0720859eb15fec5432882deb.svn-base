package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetPromosResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetPromosAPI {
    @FormUrlEncoded
    @POST("getPromos")
    Call<GetPromosResponse> getPromosCall(@Field("imei") String imei,
                                          @Field("usertype") String usertype,
                                          @Field("authcode") String authcode,
                                          @Field("sessionid") String sessionid,
                                          @Field("limit") String limit,
                                          @Field("pricegroup") String pricegroup,
                                          @Field("customerid") String customerid);
}
