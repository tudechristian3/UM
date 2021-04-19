package com.ultramega.shop.services;


import com.ultramega.shop.responses.CancelIndividualOrderResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CancelIndividualOrderAPI {
    @FormUrlEncoded
    @POST("deleteIndividualItemOrder")
    Call<CancelIndividualOrderResponse> cancelIndividualOrderCall(@Field("imei") String imei,
                                                                  @Field("authcode") String authcode,
                                                                  @Field("sessionid") String sessionid,
                                                                  @Field("customerid") String customerid,
                                                                  @Field("usertype") String usertype,
                                                                  @Field("ordertxnid") String ordertxnid,
                                                                  @Field("itemid") String itemid,
                                                                  @Field("userid") String userid,
                                                                  @Field("catclass") String sku,
                                                                  @Field("packageid") String packageid);
}
