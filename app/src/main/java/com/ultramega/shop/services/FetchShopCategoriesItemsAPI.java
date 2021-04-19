package com.ultramega.shop.services;


import com.ultramega.shop.responses.FetchShopCategoriesItemsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FetchShopCategoriesItemsAPI {
    @FormUrlEncoded
    @POST("fetchCategoryItems")
    Call<FetchShopCategoriesItemsResponse> fetchShopCategoriesItemsCall(@Field("imei") String imei,
                                                                        @Field("categorytype") String categorytype,
                                                                        @Field("usertype") String usertype,
                                                                        @Field("authcode") String authcode,
                                                                        @Field("sessionid") String sessionid,
                                                                        @Field("categoryid") String categoryid,
                                                                        @Field("limit") String limit,
                                                                        @Field("pricegroup") String pricegroup);
}
