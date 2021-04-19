package com.ultramega.shop.services;


import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FetchShoppingCartsQueueAPI {
    @FormUrlEncoded
    @POST("fetchShoppingCarts")
    Call<FetchShoppingCartsQueueResponse> fetchShoppingCartsQueueCall(@Field("imei") String imei,
                                                                      @Field("authcode") String authcode,
                                                                      @Field("limit") String limit,
                                                                      @Field("sessionid") String sessionid,
                                                                      @Field("userid") String userid,
                                                                      @Field("customerid") String customerid,
                                                                      @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("fetchCountsShoppingCarts")
    Call<FetchShoppingCartsQueueResponse> fetchCountsShoppingCartsQueueCall
                                                                      (@Field("imei") String imei,
                                                                      @Field("authcode") String authcode,
                                                                      @Field("limit") String limit,
                                                                      @Field("sessionid") String sessionid,
                                                                      @Field("userid") String userid,
                                                                      @Field("customerid") String customerid,
                                                                      @Field("usertype") String usertype);
}
