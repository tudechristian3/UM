package com.ultramega.shop.services;


import com.ultramega.shop.responses.GetDailyFindsResponse;
import com.ultramega.shop.responses.GetPopularSearchesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetDailyFindsAPI {
    @FormUrlEncoded
    @POST("getDailyFinds")
    Call<GetDailyFindsResponse> getDailyFindsCall(@Field("imei") String imei,
                                                  @Field("usertype") String usertype,
                                                  @Field("authcode") String authcode,
                                                  @Field("sessionid") String sessionid,
                                                  @Field("limit") String limit,
                                                  @Field("pricegroup") String pricegroup,
                                                  @Field("customerid") String customerid);

    @FormUrlEncoded
    @POST("getSearchItems")
    Call<GetDailyFindsResponse> search(@Field("imei") String imei,
                                       @Field("usertype") String usertype,
                                       @Field("authcode") String authcode,
                                       @Field("sessionid") String sessionid,
                                       @Field("limit") int limit,
                                       @Field("pricegroup") String pricegroup,
                                       @Field("customerid") String customerid,
                                       @Field("userid") String userid,
                                       @Field("searchvalue") String searchvalue,
                                       @Field("searchtype") String searchtype);

    @FormUrlEncoded
    @POST("getPopularSearches")
    Call<GetPopularSearchesResponse> getPopularSearches(@Field("imei") String imei,
                                                        @Field("usertype") String usertype,
                                                        @Field("authcode") String authcode,
                                                        @Field("sessionid") String sessionid);
}
