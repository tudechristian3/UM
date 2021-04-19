package com.ultramega.shop.services;


import com.ultramega.shop.responses.RegisterWholeSalerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterWholeSalerAPI {
    @FormUrlEncoded
    @POST("registerWholeSaler")
    Call<RegisterWholeSalerResponse> registerWholeSalerCall(@Field("imei") String imei,
                                                            @Field("authcode") String authcode,
                                                            @Field("sessionid") String sessionid,
                                                            @Field("branchid") String branchid,
                                                            @Field("mobilenumber") String mobilenumber,
                                                            @Field("businessname") String businessname,
                                                            @Field("firstname") String firstname,
                                                            @Field("lastname") String lastname,
                                                            @Field("middlename") String middlename,
                                                            @Field("country") String country,
                                                            @Field("countrycode") String countrycode,
                                                            @Field("profilepic") String profilepic,
                                                            @Field("streetaddress") String streetaddress,
                                                            @Field("city") String city,
                                                            @Field("gender") String gender,
                                                            @Field("province") String province,
                                                            @Field("emailaddress") String emailaddress,
                                                            @Field("birthdate") String birthdate);


    @FormUrlEncoded
    @POST("updateWholesalerProfilePic")
    Call<RegisterWholeSalerResponse> updateWholesalerProfilePicCall(@Field("wholesalerid") String wholesalerid,
                                                                    @Field("userid") String userid,
                                                                    @Field("authcode") String authcode,
                                                                    @Field("imei") String imei,
                                                                    @Field("sessionid") String sessionid,
                                                                    @Field("profilepic") String profilepic);

}
