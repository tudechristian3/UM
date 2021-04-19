package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetPromoItemsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetPromoItemsAPI {
    @FormUrlEncoded
    @POST("getPromoItems")
    Call<GetPromoItemsResponse> getPromoItemsCall(@Field("imei") String imei,
                                                  @Field("usertype") String usertype,
                                                  @Field("authcode") String authcode,
                                                  @Field("sessionid") String sessionid,
                                                  @Field("limit") String limit,
                                                  @Field("pricegroup") String pricegroup,
                                                  @Field("promoid") String promoid);
}
