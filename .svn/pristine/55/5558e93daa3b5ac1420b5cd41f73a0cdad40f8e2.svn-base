package com.ultramega.shop.services;


import com.ultramega.shop.responses.FetchShopCategoriesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FetchShopCategoriesAPI {
    @FormUrlEncoded
    @POST("fetchShopCategories")
    Call<FetchShopCategoriesResponse> fetchShopCategoriesCall(@Field("imei") String imei,
                                                              @Field("usertype") String usertype,
                                                              @Field("authcode") String authcode,
                                                              @Field("limit") String limit,
                                                              @Field("sessionid") String sessionid,
                                                              @Field("pricegroup") String pricegroup);
}
