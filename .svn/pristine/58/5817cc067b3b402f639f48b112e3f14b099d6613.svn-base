package com.ultramega.shop.services;


import com.ultramega.shop.responses.CancelWholeOrderResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CancelWholeOrderAPI {
    @FormUrlEncoded
    @POST("deleteWholeItemOrder")
    Call<CancelWholeOrderResponse> cancelWholeOrderCall(@Field("imei") String imei,
                                                        @Field("authcode") String authcode,
                                                        @Field("sessionid") String sessionid,
                                                        @Field("customerid") String customerid,
                                                        @Field("usertype") String usertype,
                                                        @Field("ordertxnid") String ordertxnid,
                                                        @Field("userid") String userid);
}
