package com.ultramega.shop.services;

import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetSessionResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User-PC on 7/1/2017.
 */

public interface CommonAPI {

    @FormUrlEncoded
    @POST("createUnregisteredSession")
    Call<GetSessionResponse> fetchUnregisteredSessionCall(@Field("imei") String imei);

    @FormUrlEncoded
    @POST("createRegisteredSession")
    Call<GetSessionResponse> fetchRegisteredSessionCall(@Field("imei") String imei,
                                                        @Field("userid") String userid,
                                                        @Field("usertype") String usertype,
                                                        @Field("mobilenumber") String mobilenumber);

    @FormUrlEncoded
    @POST("updateStatusReloadWalletViaPesoPay")
    Call<GenericResponse> updateStatusReloadWalletViaPesoPay(@Field("imei") String imei,
                                                             @Field("userid") String userid,
                                                             @Field("authcode") String authcode,
                                                             @Field("sessionid") String sessionid,
                                                             @Field("paymenttxnno") String txnno,
                                                             @Field("customerid") String customerid,
                                                             @Field("status") String status);

}
