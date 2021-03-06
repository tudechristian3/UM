package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetBranchesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetBranchesAPI {
    @FormUrlEncoded
    @POST("getBranches")
    Call<GetBranchesResponse> getBranchesCall(@Field("authcode") String authcode,
                                              @Field("imei") String imei,
                                              @Field("sessionid") String sessionid);

    @FormUrlEncoded
    @POST("getBranchesForRetailer")
    Call<GetBranchesResponse> getBranchesForRetailer(
                                              @Field("authcode") String authcode,
                                              @Field("imei") String imei,
                                              @Field("sessionid") String sessionid,
                                              @Field("processoption") String processoption);
}
