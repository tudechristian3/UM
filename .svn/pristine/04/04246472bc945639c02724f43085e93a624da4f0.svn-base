package com.ultramega.shop.services;


import com.google.android.gms.common.annotation.KeepForSdkWithFieldsAndMethods;
import com.ultramega.shop.responses.CheckCardPaymentStatusResponse;
import com.ultramega.shop.responses.GetItemPackagesResponse;
import com.ultramega.shop.responses.GetShoppingModeConfigResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UltramegaShopperAPI {
    @FormUrlEncoded
    @POST("getItemPackages")
    Call<GetItemPackagesResponse> getItemPackagesCall(@Field("imei") String imei,
                                                      @Field("authcode") String authcode,
                                                      @Field("sessionid") String sessionid,
                                                      @Field("customerid") String customerid,
                                                      @Field("usertype") String usertype,
                                                      @Field("userid") String userid,
                                                      @Field("catclass") String catclass,
                                                      @Field("wholesalerpricegroup") String wholesalerpricegroup,
                                                      @Field("itemcode") String itemcode);

    @FormUrlEncoded
    @POST("checkCardPaymentStatus")
    Call<CheckCardPaymentStatusResponse> checkCardPaymentStatusCall(@Field("imei") String imei,
                                                                    @Field("authcode") String authcode,
                                                                    @Field("sessionid") String sessionid,
                                                                    @Field("customerid") String customerid,
                                                                    @Field("usertype") String usertype,
                                                                    @Field("userid") String userid,
                                                                    @Field("ordertxnid") String ordertxnid);

    @FormUrlEncoded
    @POST("getShoppingModeConfig")
    Call<GetShoppingModeConfigResponse> getShoppingModeConfigCall(@Field("imei") String imei,
                                                                  @Field("authcode") String authcode,
                                                                  @Field("sessionid") String sessionid,
                                                                  @Field("appversion") String appversion,
                                                                  @Field("devicetype") String devicetype);
}
