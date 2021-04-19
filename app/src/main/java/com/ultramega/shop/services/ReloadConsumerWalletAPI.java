package com.ultramega.shop.services;


import com.ultramega.shop.responses.PesoPayDataResponse;
import com.ultramega.shop.responses.ReloadConsumerWalletResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReloadConsumerWalletAPI {
    @FormUrlEncoded
    @POST("reloadConsumerWallet")
    Call<ReloadConsumerWalletResponse> reloadConsumerWalletCall(@Field("consumerid") String consumerid,
                                                                @Field("userid") String userid,
                                                                @Field("authcode") String authcode,
                                                                @Field("imei") String imei,
                                                                @Field("sessionid") String sessionid,
                                                                @Field("consumername") String consumername,
                                                                @Field("customerremarks") String customerremarks,
                                                                @Field("paymentoption") String paymentoption,
                                                                @Field("depositslipurl") String depositslipurl,
                                                                @Field("banktxnnumber") String banktxnnumber,
                                                                @Field("bankname") String bankname,
                                                                @Field("bankcode") String bankcode,
                                                                @Field("bankaccountname") String bankaccountname,
                                                                @Field("bankaccountnumber") String bankaccountnumber,
                                                                @Field("depositdatetime") String depositdatetime,
                                                                @Field("amountpaid") String amountpaid);

    @FormUrlEncoded
    @POST("reloadConsumerWalletViaPesoPay")
    Call<PesoPayDataResponse> reloadConsumerWalletViaPesoPay(@Field("consumerid") String consumerid,
                                                             @Field("userid") String userid,
                                                             @Field("authcode") String authcode,
                                                             @Field("imei") String imei,
                                                             @Field("sessionid") String sessionid,
                                                             @Field("consumername") String consumername,
                                                             @Field("customerremarks") String customerremarks,
                                                             @Field("paymentoption") String paymentoption,
                                                             @Field("amountpaid") String amountpaid);
}
