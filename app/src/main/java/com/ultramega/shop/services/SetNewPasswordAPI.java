package com.ultramega.shop.services;


import com.ultramega.shop.responses.SetNewPasswordResponse;
import com.ultramega.shop.responses.SetNewPasswordWholeSalerResponse;
import com.ultramega.shop.responses.UpdatePasswordResponse;
import com.ultramega.shop.responses.UpdatePasswordWholeSalerResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SetNewPasswordAPI {
    @FormUrlEncoded
    @POST("setNewPassword")
    Call<SetNewPasswordResponse> setNewPasswordCall(@Field("imei") String imei,
                                                    @Field("authcode") String authcode,
                                                    @Field("sessionid") String sessionid,
                                                    @Field("password") String password,
                                                    @Field("userid") String userid,
                                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("setNewPasswordWholeSaler")
    Call<SetNewPasswordWholeSalerResponse> setNewPasswordWholeSalerCall(@Field("imei") String imei,
                                                                        @Field("authcode") String authcode,
                                                                        @Field("sessionid") String sessionid,
                                                                        @Field("password") String password,
                                                                        @Field("userid") String userid,
                                                                        @Field("token") String token);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<UpdatePasswordResponse> updatePasswordCall(@Field("imei") String imei,
                                                    @Field("authcode") String authcode,
                                                    @Field("sessionid") String sessionid,
                                                    @Field("password") String password,
                                                    @Field("userid") String userid,
                                                    @Field("oldpassword") String oldpassword,
                                                    @Field("usertype") String usertype);

    @FormUrlEncoded
    @POST("updatePasswordWholeSaler")
    Call<UpdatePasswordWholeSalerResponse> updatePasswordWholeSalerCall(@Field("imei") String imei,
                                                                        @Field("authcode") String authcode,
                                                                        @Field("sessionid") String sessionid,
                                                                        @Field("password") String password,
                                                                        @Field("userid") String userid,
                                                                        @Field("oldpassword") String oldpassword,
                                                                        @Field("usertype") String usertype);
}
