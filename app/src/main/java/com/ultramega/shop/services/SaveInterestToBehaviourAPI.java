package com.ultramega.shop.services;


import com.ultramega.shop.responses.SaveInterestToBehaviourResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SaveInterestToBehaviourAPI {
    @FormUrlEncoded
    @POST("saveInterestToBehaviour")
    Call<SaveInterestToBehaviourResponse> saveInterestToBehaviourCall(@Field("imei") String imei,
                                                                      @Field("usertype") String usertype,
                                                                      @Field("sessionid") String sessionid,
                                                                      @Field("authcode") String authcode,
                                                                      @Field("otherinterest") String otherinterest);
}
