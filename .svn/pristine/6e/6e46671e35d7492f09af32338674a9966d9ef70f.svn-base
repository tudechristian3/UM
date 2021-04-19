package com.ultramega.shop.services;


import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.UpdateConsumerProfileResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateConsumerProfileResponseAPI {
    @FormUrlEncoded
    @POST("updateConsumerProfile")
    Call<UpdateConsumerProfileResponse> updateConsumerProfileCall(@Field("sessionid") String sessionid,
                                                                  @Field("consumerid") String consumerid,
                                                                  @Field("userid") String userid,
                                                                  @Field("mobilenumber") String mobilenumber,
                                                                  @Field("authcode") String authcode,
                                                                  @Field("imei") String imei,
                                                                  @Field("firstname") String firstname,
                                                                  @Field("lastname") String lastname,
                                                                  @Field("countrycode") String countrycode,
                                                                  @Field("country") String country,
                                                                  @Field("birthdate") String birthdate,
                                                                  @Field("gender") String gender,
                                                                  @Field("occupation") String occupation,
                                                                  @Field("interest") String interest,
                                                                  @Field("emailaddress") String emailaddress);

    @FormUrlEncoded
    @POST("updateConsumerProfilePic")
    Call<GenericResponse> updateConsumerProfilePic(@Field("sessionid") String sessionid,
                                                   @Field("consumerid") String consumerid,
                                                   @Field("userid") String userid,
                                                   @Field("authcode") String authcode,
                                                   @Field("imei") String imei,
                                                   @Field("profilepic") String profilepic);
}
