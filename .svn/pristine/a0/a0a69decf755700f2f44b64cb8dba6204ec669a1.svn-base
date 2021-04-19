package com.ultramega.shop.services;


import com.ultramega.shop.responses.FetchShopCategoriesItemSKUResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FetchShopCategoriesItemSKUAPI {
    @FormUrlEncoded
    @POST("fetchItemSKU")
    Call<FetchShopCategoriesItemSKUResponse> fetchShopCategoriesItemSKUCall(@Field("imei") String imei,
                                                                            @Field("authcode") String authcode,
                                                                            @Field("sessionid") String sessionid,
                                                                            @Field("catclass") String catclass,
                                                                            @Field("usertype") String usertype,
                                                                            @Field("customerid") String customerid,
                                                                            @Field("promoid") String promoid,
                                                                            @Field("wholesalerpricegroup") String wholesalerpricegroup,
                                                                            @Field("supplierid") String supplierid,
                                                                            @Field("limit") String limit);
}
