package com.ultramega.shop.services;


import com.ultramega.shop.responses.AddToMyListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddToMyListAPI {
    @FormUrlEncoded
    @POST("addToMyList")
    Call<AddToMyListResponse> addToMyListCall(@Field("imei") String imei,
                                              @Field("authcode") String authcode,
                                              @Field("sessionid") String sessionid,
                                              @Field("customerid") String customerid,
                                              @Field("usertype") String usertype,
                                              @Field("catclass") String catclass,
                                              @Field("catclasspicurl") String catclasspicurl,
                                              @Field("userid") String userid,
                                              @Field("type") String type);
}
