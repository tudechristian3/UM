package com.ultramega.shop.services;


import com.ultramega.shop.responses.AddUdateShoppingCartResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddUpdateShoppingCartAPI {
    @FormUrlEncoded
    @POST("addUpdateShoppingCart")
    Call<AddUdateShoppingCartResponse> addUpdateShoppingCartCall(@Field("imei") String imei,
                                                                 @Field("authcode") String authcode,
                                                                 @Field("sessionid") String sessionid,
                                                                 @Field("customerid") String customerid,
                                                                 @Field("usertype") String usertype,
                                                                 @Field("itemid") String itemid,
                                                                 @Field("quantity") int quantity,
                                                                 @Field("userid") String userid,
                                                                 @Field("packageid") String packageid,
                                                                 @Field("itempicurl") String itempicurl);
}
