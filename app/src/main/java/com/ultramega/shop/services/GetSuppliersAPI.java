package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetSuppliersResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetSuppliersAPI {
    @FormUrlEncoded
    @POST("getSuppliers")
    Call<GetSuppliersResponse> getSuppliersCall(@Field("imei") String imei,
                                                @Field("sessionid") String sessionid,
                                                @Field("authcode") String authcode,
                                                @Field("limit") String limit,
                                                @Field("usertype") String usertype,
                                                @Field("pricegroup") String pricegroup);
}
