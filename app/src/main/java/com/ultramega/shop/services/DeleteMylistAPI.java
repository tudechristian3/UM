package com.ultramega.shop.services;

import com.ultramega.shop.responses.DeleteMyListResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Tony on 23/08/2017.
 */

public interface DeleteMylistAPI {

    @FormUrlEncoded
    @POST("deleteMyList")
    Call<DeleteMyListResponse> deleteMyListCall (@Field("imei") String imei,
                                              @Field("authcode") String authcode,
                                              @Field("sessionid") String sessionid,
                                              @Field("customerid") String customerid,
                                              @Field("usertype") String usertype,
                                              @Field("userid") String userid,
                                              @Field("catclass") String itemid);
}
