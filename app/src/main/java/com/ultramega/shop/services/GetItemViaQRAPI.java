package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetItemViaQRResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetItemViaQRAPI {
    @FormUrlEncoded
    @POST("getItemViaQR")
    Call<GetItemViaQRResponse> getItemViaQRCall(@Field("imei") String imei,
                                                @Field("authcode") String authcode,
                                                @Field("sessionid") String sessionid,
                                                @Field("usertype") String usertype,
                                                @Field("barcode") String barcode,
                                                @Field("pricegroup") String pricegroup);
}
