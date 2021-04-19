package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetPromoPointsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetPromoPointsAPI {
    @FormUrlEncoded
    @POST("getPromoPoints")
    Call<GetPromoPointsResponse> getPromoPointsCall(@Field("imei") String imei,
                                                    @Field("authcode") String authcode,
                                                    @Field("sessionid") String sessionid,
                                                    @Field("usertype") String usertype,
                                                    @Field("userid") String userid,
                                                    @Field("customerid") String customerid,
                                                    @Field("limit") String limit);
}
