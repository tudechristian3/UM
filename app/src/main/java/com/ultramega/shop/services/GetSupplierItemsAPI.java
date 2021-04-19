package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetSupplierItemsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetSupplierItemsAPI {
    @FormUrlEncoded
    @POST("getSupplierItems")
    Call<GetSupplierItemsResponse> getSupplierItemsCall(@Field("imei") String imei,
                                                        @Field("usertype") String usertype,
                                                        @Field("authcode") String authcode,
                                                        @Field("sessionid") String sessionid,
                                                        @Field("supplierid") String supplierid,
                                                        @Field("limit") String limit,
                                                        @Field("pricegroup") String pricegroup);
}
