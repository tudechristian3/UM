package com.ultramega.shop.rest;

import android.content.Context;

import com.ultramega.shop.services.AddConsumerAddressAPI;
import com.ultramega.shop.services.AddOrdersAPI;
import com.ultramega.shop.services.AddToMyListAPI;
import com.ultramega.shop.services.AddUpdateShoppingCartAPI;
import com.ultramega.shop.services.CancelIndividualOrderAPI;
import com.ultramega.shop.services.CancelTransactionAPI;
import com.ultramega.shop.services.CancelWholeOrderAPI;
import com.ultramega.shop.services.CommonAPI;
import com.ultramega.shop.services.ConfirmOrderChangesAPI;
import com.ultramega.shop.services.DeleteMylistAPI;
import com.ultramega.shop.services.DeleteShoppingCartAPI;
import com.ultramega.shop.services.FetchShopCategoriesAPI;
import com.ultramega.shop.services.FetchShopCategoriesItemSKUAPI;
import com.ultramega.shop.services.FetchShopCategoriesItemsAPI;
import com.ultramega.shop.services.FetchShoppingCartsQueueAPI;
import com.ultramega.shop.services.GetAccountInformationAPI;
import com.ultramega.shop.services.GetBankReferenceAPI;
import com.ultramega.shop.services.GetBranchesAPI;
import com.ultramega.shop.services.GetConsumerAddressAPI;
import com.ultramega.shop.services.GetConsumerWalletAPI;
import com.ultramega.shop.services.GetCustomerMyListAPI;
import com.ultramega.shop.services.GetDailyFindsAPI;
import com.ultramega.shop.services.GetItemViaQRAPI;
import com.ultramega.shop.services.GetNotificationsAPI;
import com.ultramega.shop.services.GetOrdersAPI;
import com.ultramega.shop.services.GetPaymentsResponseAPI;
import com.ultramega.shop.services.GetPromoItemsAPI;
import com.ultramega.shop.services.GetPromoPointsAPI;
import com.ultramega.shop.services.GetPromosAPI;
import com.ultramega.shop.services.GetS3keyAPI;
import com.ultramega.shop.services.GetSupplierItemsAPI;
import com.ultramega.shop.services.GetSuppliersAPI;
import com.ultramega.shop.services.LoginAPI;
import com.ultramega.shop.services.PartialRegisterWholeSalerAPI;
import com.ultramega.shop.services.PartialRegistrationEnterAccessCodeAPI;
import com.ultramega.shop.services.PartialRegistrationEnterMobileAPI;
import com.ultramega.shop.services.PayOrderAPI;
import com.ultramega.shop.services.RegisterConsumerAPI;
import com.ultramega.shop.services.RegisterWholeSalerAPI;
import com.ultramega.shop.services.ReloadConsumerWalletAPI;
import com.ultramega.shop.services.SaveInterestToBehaviourAPI;
import com.ultramega.shop.services.SetNewPasswordAPI;
import com.ultramega.shop.services.UltramegaShopperAPI;
import com.ultramega.shop.services.UpayAPI;
import com.ultramega.shop.services.UpdateConsumerProfileResponseAPI;
import com.ultramega.shop.services.UpdateNotificationStatusAPI;
import com.ultramega.shop.services.ViewPaymentWalletAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuild {
    private static FetchShopCategoriesAPI fetchShopCategoriesService;
    private static FetchShopCategoriesItemsAPI fetchShopCategoriesItemsService;
    private static FetchShopCategoriesItemSKUAPI fetchShopCategoriesItemSKUService;
    private static AddUpdateShoppingCartAPI addUpdateShoppingCartService;
    private static FetchShoppingCartsQueueAPI fetchShoppingCartsQueueService;
    private static DeleteShoppingCartAPI deleteItemInShoppingCartService;
    private static PartialRegistrationEnterMobileAPI partialRegistrationEnterMobileService;
    private static PartialRegistrationEnterAccessCodeAPI partialRegistrationEnterAccessCodeService;
    private static RegisterConsumerAPI registerConsumerService;
    private static AddOrdersAPI addOrdersService;
    private static LoginAPI loginService;
    private static ReloadConsumerWalletAPI reloadConsumerWalletService;
    private static GetBankReferenceAPI getBankReferenceService;
    private static GetConsumerAddressAPI getConsumerAddressService;
    private static GetOrdersAPI getOrdersQueueService;
    private static PayOrderAPI payOrderService;
    private static GetItemViaQRAPI getItemViaQRService;
    private static GetConsumerWalletAPI getConsumerWalletService;
    private static CancelIndividualOrderAPI cancelIndividualorderService;
    private static CancelWholeOrderAPI cancelWholeOrderService;
    private static AddConsumerAddressAPI addConsumerAddressService;
    private static AddToMyListAPI addToMyListService;
    private static DeleteMylistAPI deleteMyListService;
    private static GetCustomerMyListAPI getCustomerMyListService;
    private static UpdateConsumerProfileResponseAPI updateConsumerProfileService;
    private static GetBranchesAPI getBranchesService;
    private static PartialRegisterWholeSalerAPI partialRegisterWholeSalerService;
    private static GetAccountInformationAPI getAccountInformationService;
    private static RegisterWholeSalerAPI registerWholeSalerService;
    private static GetSuppliersAPI getSuppliersService;
    private static GetSupplierItemsAPI getSupplierItemsService;
    private static GetDailyFindsAPI getDailyFindsService;
    private static GetPromosAPI getPromosService;
    private static GetPromoItemsAPI getPromoItemsService;
    private static SetNewPasswordAPI setNewPasswordService;
    private static SetNewPasswordAPI setNewPasswordWholeSalerService;
    private static GetPaymentsResponseAPI getPaymentsService;
    private static GetPromoPointsAPI getPromoPointsService;
    private static GetNotificationsAPI getNotificationsService;
    private static ViewPaymentWalletAPI getViewPaymentService;
    private static SaveInterestToBehaviourAPI saveInterestToBehaviourService;
    private static ConfirmOrderChangesAPI confirmOrderChangesService;
    private static UpdateNotificationStatusAPI updateNotificationStatusService;
    private static CancelTransactionAPI cancelTransactionService;
    private static SetNewPasswordAPI updatePasswordService;
    private static SetNewPasswordAPI updatePasswordWholeSalerService;
    private static GetS3keyAPI getS3keyService;
    private static CommonAPI commonApiService;
    private static UltramegaShopperAPI getItemPackagesService;
    private static UltramegaShopperAPI checkCardPaymentStatusService;
    private static UltramegaShopperAPI getShoppingModeConfigService;
    private static UpayAPI upayAPI;

    //==========================================================================================
    //PRODUCTION
    //==============
//    public static final String ROOT_URL = "https://wsshop.ultramega.store/";
//    private static final String PUSH_URL = "https://wspush.ultramega.store/";
//    public static final String S3_URL_RETAILER = "https://s3-us-west-1.amazonaws.com/ultramega-items-final/";
//    public static final String S3_URL_WHOLESALER = "https://s3-us-west-1.amazonaws.com/ultramega-wholesale-items-final/";
//    public static final String AWS_BUCKETNAME = "ultramega-profile-final";
    //==========================================================================================

    //==========================================================================================
    //STAGING
    //==============
    public static final String ROOT_URL = "http://staging-wsshop.ultramega.godvapps.com/";
    public static final String PUSH_URL = "http://staging-wspush.ultramega.godvapps.com/";
    public static final String S3_URL_RETAILER = "https://s3-us-west-1.amazonaws.com/ultramega-items-final/";
    public static final String S3_URL_WHOLESALER = "https://s3-us-west-1.amazonaws.com/ultramega-wholesale-items-final/";
    public static final String AWS_BUCKETNAME = "ultramega-profile-final";

    //==============
    //DEVELOPMENT
//    ==============
//    public static final String ROOT_URL = "http://17
//    2.16.16.100:8088/";
//    public static final String PUSH_URL = "http://staging-wspush.ultramega.godvapps.com/";
//    public static final String S3_URL_RETAILER = "https://s3-us-west-1.amazonaws.com/ultramega-items/";
//    public static final String S3_URL_WHOLESALER = "https://s3-us-west-1.amazonaws.com/ultramega-wholesale-items/";
//    public static final String AWS_BUCKETNAME = "ultramega-profile";
    //==========================================================================================




    private static Retrofit retrofitBuilder = null;

    public static final String appversion = "1";
    public static final String devicetype = "ANDROID";

    private static Retrofit getRetrofitBuilder(final Context context) {
        if (retrofitBuilder == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(interceptor);

            retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitBuilder;
    }

    public static GetSupplierItemsAPI getSupplierItemsService(Context context) {
        if (getSupplierItemsService == null) {
            getSupplierItemsService = getRetrofitBuilder(context).create(GetSupplierItemsAPI.class);
        }
        return getSupplierItemsService;
    }

    public static GetSuppliersAPI getSuppliersService(Context context) {
        if (getSuppliersService == null) {
            getSuppliersService = getRetrofitBuilder(context).create(GetSuppliersAPI.class);
        }
        return getSuppliersService;
    }

    public static RegisterWholeSalerAPI registerWholeSalerService(Context context) {
        if (registerWholeSalerService == null) {
            registerWholeSalerService = getRetrofitBuilder(context).create(RegisterWholeSalerAPI.class);
        }
        return registerWholeSalerService;
    }

    public static GetAccountInformationAPI getAccountInformationService(Context context) {
        if (getAccountInformationService == null) {
            getAccountInformationService = getRetrofitBuilder(context).create(GetAccountInformationAPI.class);
        }
        return getAccountInformationService;
    }

    public static PartialRegisterWholeSalerAPI partialRegisterWholeSalerService(Context context) {
        if (partialRegisterWholeSalerService == null) {
            partialRegisterWholeSalerService = getRetrofitBuilder(context).create(PartialRegisterWholeSalerAPI.class);
        }
        return partialRegisterWholeSalerService;
    }

    public static GetBranchesAPI getBranchesService(Context context) {
        if (getBranchesService == null) {
            getBranchesService = getRetrofitBuilder(context).create(GetBranchesAPI.class);
        }
        return getBranchesService;
    }

    public static UpdateConsumerProfileResponseAPI updateConsumerProfileService(Context context) {
        if (updateConsumerProfileService == null) {
            updateConsumerProfileService = getRetrofitBuilder(context).create(UpdateConsumerProfileResponseAPI.class);
        }
        return updateConsumerProfileService;
    }

    public static GetCustomerMyListAPI getCustomerMyListService(Context context) {
        if (getCustomerMyListService == null) {
            getCustomerMyListService = getRetrofitBuilder(context).create(GetCustomerMyListAPI.class);
        }
        return getCustomerMyListService;
    }

    public static AddToMyListAPI addToMyListService(Context context) {
        if (addToMyListService == null) {
            addToMyListService = getRetrofitBuilder(context).create(AddToMyListAPI.class);
        }
        return addToMyListService;
    }

    public static DeleteMylistAPI deleteMyListService(Context context) {
        if (deleteMyListService == null) {
            deleteMyListService = getRetrofitBuilder(context).create(DeleteMylistAPI.class);
        }
        return deleteMyListService;
    }

    public static AddConsumerAddressAPI addConsumerAddressService(Context context) {
        if (addConsumerAddressService == null) {
            addConsumerAddressService = getRetrofitBuilder(context).create(AddConsumerAddressAPI.class);
        }
        return addConsumerAddressService;
    }

    public static CancelWholeOrderAPI cancelWholeOrderService(Context context) {
        if (cancelWholeOrderService == null) {
            cancelWholeOrderService = getRetrofitBuilder(context).create(CancelWholeOrderAPI.class);
        }
        return cancelWholeOrderService;
    }

    public static CancelIndividualOrderAPI cancelIndividualorderService(Context context) {
        if (cancelIndividualorderService == null) {
            cancelIndividualorderService = getRetrofitBuilder(context).create(CancelIndividualOrderAPI.class);
        }
        return cancelIndividualorderService;
    }

    public static GetConsumerWalletAPI getConsumerWalletService(Context context) {
        if (getConsumerWalletService == null) {
            getConsumerWalletService = getRetrofitBuilder(context).create(GetConsumerWalletAPI.class);
        }
        return getConsumerWalletService;
    }

    public static GetItemViaQRAPI getItemViaQRService(Context context) {
        if (getItemViaQRService == null) {
            getItemViaQRService = getRetrofitBuilder(context).create(GetItemViaQRAPI.class);
        }
        return getItemViaQRService;
    }

    public static PayOrderAPI payOrderService(Context context) {
        if (payOrderService == null) {
            payOrderService = getRetrofitBuilder(context).create(PayOrderAPI.class);
        }
        return payOrderService;
    }

    public static GetOrdersAPI getOrdersQueueService(Context context) {
        if (getOrdersQueueService == null) {
            getOrdersQueueService = getRetrofitBuilder(context).create(GetOrdersAPI.class);
        }
        return getOrdersQueueService;
    }

    public static GetConsumerAddressAPI getConsumerAddressService(Context context) {
        if (getConsumerAddressService == null) {
            getConsumerAddressService = getRetrofitBuilder(context).create(GetConsumerAddressAPI.class);
        }
        return getConsumerAddressService;
    }

    public static GetBankReferenceAPI getBankReferenceService(Context context) {
        if (getBankReferenceService == null) {
            getBankReferenceService = getRetrofitBuilder(context).create(GetBankReferenceAPI.class);
        }
        return getBankReferenceService;
    }

    public static ReloadConsumerWalletAPI reloadConsumerWalletService(Context context) {
        if (reloadConsumerWalletService == null) {
            reloadConsumerWalletService = getRetrofitBuilder(context).create(ReloadConsumerWalletAPI.class);
        }
        return reloadConsumerWalletService;
    }

    public static LoginAPI loginService(Context context) {
        if (loginService == null) {
            loginService = getRetrofitBuilder(context).create(LoginAPI.class);
        }
        return loginService;
    }

    public static AddOrdersAPI addOrdersService(Context context) {
        if (addOrdersService == null) {
            addOrdersService = getRetrofitBuilder(context).create(AddOrdersAPI.class);
        }
        return addOrdersService;
    }

    public static RegisterConsumerAPI registerConsumerService(Context context) {
        if (registerConsumerService == null) {
            registerConsumerService = getRetrofitBuilder(context).create(RegisterConsumerAPI.class);
        }
        return registerConsumerService;
    }

    public static PartialRegistrationEnterAccessCodeAPI partialRegistrationEnterAccessCodeService(Context context) {
        if (partialRegistrationEnterAccessCodeService == null) {
            partialRegistrationEnterAccessCodeService = getRetrofitBuilder(context).create(PartialRegistrationEnterAccessCodeAPI.class);
        }
        return partialRegistrationEnterAccessCodeService;
    }

    public static PartialRegistrationEnterMobileAPI partialRegistrationEnterMobileService(Context context) {
        if (partialRegistrationEnterMobileService == null) {
            partialRegistrationEnterMobileService = getRetrofitBuilder(context).create(PartialRegistrationEnterMobileAPI.class);
        }
        return partialRegistrationEnterMobileService;
    }

    public static DeleteShoppingCartAPI deleteItemInShoppingCartService(Context context) {
        if (deleteItemInShoppingCartService == null) {
            deleteItemInShoppingCartService = getRetrofitBuilder(context).create(DeleteShoppingCartAPI.class);
        }
        return deleteItemInShoppingCartService;
    }

    public static FetchShoppingCartsQueueAPI fetchShoppingCartsQueueService(Context context) {
        if (fetchShoppingCartsQueueService == null) {
            fetchShoppingCartsQueueService = getRetrofitBuilder(context).create(FetchShoppingCartsQueueAPI.class);
        }
        return fetchShoppingCartsQueueService;
    }

    public static AddUpdateShoppingCartAPI addUpdateShoppingCartService(Context context) {
        if (addUpdateShoppingCartService == null) {
            addUpdateShoppingCartService = getRetrofitBuilder(context).create(AddUpdateShoppingCartAPI.class);
        }
        return addUpdateShoppingCartService;
    }

    public static FetchShopCategoriesItemSKUAPI fetchShopCategoriesItemSKUService(Context context) {
        if (fetchShopCategoriesItemSKUService == null) {
            fetchShopCategoriesItemSKUService = getRetrofitBuilder(context).create(FetchShopCategoriesItemSKUAPI.class);
        }
        return fetchShopCategoriesItemSKUService;
    }

    public static FetchShopCategoriesItemsAPI fetchShopCategoriesItemsService(Context context) {
        if (fetchShopCategoriesItemsService == null) {
            fetchShopCategoriesItemsService = getRetrofitBuilder(context).create(FetchShopCategoriesItemsAPI.class);
        }
        return fetchShopCategoriesItemsService;
    }

    public static FetchShopCategoriesAPI fetchShopCategoriesService(Context context) {
        if (fetchShopCategoriesService == null) {
            fetchShopCategoriesService = getRetrofitBuilder(context).create(FetchShopCategoriesAPI.class);
        }
        return fetchShopCategoriesService;
    }

    public static CommonAPI commonApiService(Context context) {
        if (commonApiService == null) {
            commonApiService = getRetrofitBuilder(context).create(CommonAPI.class);
        }
        return commonApiService;
    }

    public static GetDailyFindsAPI getDailyFindsService(Context context) {
        if (getDailyFindsService == null) {
            getDailyFindsService = getRetrofitBuilder(context).create(GetDailyFindsAPI.class);
        }
        return getDailyFindsService;
    }

    public static GetPromosAPI getPromosService(Context context) {
        if (getPromosService == null) {
            getPromosService = getRetrofitBuilder(context).create(GetPromosAPI.class);
        }
        return getPromosService;
    }

    public static GetPromoItemsAPI getPromoItemsService(Context context) {
        if (getPromoItemsService == null) {
            getPromoItemsService = getRetrofitBuilder(context).create(GetPromoItemsAPI.class);
        }
        return getPromoItemsService;
    }

    public static SetNewPasswordAPI setNewPasswordService(Context context) {
        if (setNewPasswordService == null) {
            setNewPasswordService = getRetrofitBuilder(context).create(SetNewPasswordAPI.class);
        }
        return setNewPasswordService;
    }

    public static SetNewPasswordAPI setNewPasswordWholeSalerService(Context context) {
        if (setNewPasswordWholeSalerService == null) {
            setNewPasswordWholeSalerService = getRetrofitBuilder(context).create(SetNewPasswordAPI.class);
        }
        return setNewPasswordWholeSalerService;
    }

    public static GetPaymentsResponseAPI getPaymentsService(Context context) {
        if (getPaymentsService == null) {
            getPaymentsService = getRetrofitBuilder(context).create(GetPaymentsResponseAPI.class);
        }
        return getPaymentsService;
    }

    public static GetPromoPointsAPI getPromoPointsService(Context context) {
        if (getPromoPointsService == null) {
            getPromoPointsService = getRetrofitBuilder(context).create(GetPromoPointsAPI.class);
        }
        return getPromoPointsService;
    }

    public static GetNotificationsAPI getNotificationsService(Context context) {
        if (getNotificationsService == null) {
            getNotificationsService = getRetrofitBuilder(context).create(GetNotificationsAPI.class);
        }
        return getNotificationsService;
    }

    public static ViewPaymentWalletAPI getViewPaymentService(Context context) {
        if (getViewPaymentService == null) {
            getViewPaymentService = getRetrofitBuilder(context).create(ViewPaymentWalletAPI.class);
        }
        return getViewPaymentService;
    }

    public static SaveInterestToBehaviourAPI saveInterestToBehaviourService(Context context) {
        if (saveInterestToBehaviourService == null) {
            saveInterestToBehaviourService = getRetrofitBuilder(context).create(SaveInterestToBehaviourAPI.class);
        }
        return saveInterestToBehaviourService;
    }

    public static ConfirmOrderChangesAPI confirmOrderChangesService(Context context) {
        if (confirmOrderChangesService == null) {
            confirmOrderChangesService = getRetrofitBuilder(context).create(ConfirmOrderChangesAPI.class);
        }
        return confirmOrderChangesService;
    }

    public static UpdateNotificationStatusAPI updateNotificationStatusService(Context context) {
        if (updateNotificationStatusService == null) {
            updateNotificationStatusService = getRetrofitBuilder(context).create(UpdateNotificationStatusAPI.class);
        }
        return updateNotificationStatusService;
    }

    public static CancelTransactionAPI cancelTransactionService(Context context) {
        if (cancelTransactionService == null) {
            cancelTransactionService = getRetrofitBuilder(context).create(CancelTransactionAPI.class);
        }
        return cancelTransactionService;
    }

    public static SetNewPasswordAPI updatePasswordService(Context context) {
        if (updatePasswordService == null) {
            updatePasswordService = getRetrofitBuilder(context).create(SetNewPasswordAPI.class);
        }
        return updatePasswordService;
    }

    public static SetNewPasswordAPI updatePasswordWholeSalerService(Context context) {
        if (updatePasswordWholeSalerService == null) {
            updatePasswordWholeSalerService = getRetrofitBuilder(context).create(SetNewPasswordAPI.class);
        }
        return updatePasswordWholeSalerService;
    }

    public static GetS3keyAPI getS3keyService(Context context) {
        if (getS3keyService == null) {
            getS3keyService = getRetrofitBuilder(context).create(GetS3keyAPI.class);
        }
        return getS3keyService;
    }

    public static UltramegaShopperAPI getItemPackagesService(Context context) {
        if (getItemPackagesService == null) {
            getItemPackagesService = getRetrofitBuilder(context).create(UltramegaShopperAPI.class);
        }
        return getItemPackagesService;
    }

    public static UltramegaShopperAPI checkCardPaymentStatusService(Context context) {
        if (checkCardPaymentStatusService == null) {
            checkCardPaymentStatusService = getRetrofitBuilder(context).create(UltramegaShopperAPI.class);
        }
        return checkCardPaymentStatusService;
    }

    public static UltramegaShopperAPI getShoppingModeConfigService(Context context) {
        if (getShoppingModeConfigService == null) {
            getShoppingModeConfigService = getRetrofitBuilder(context).create(UltramegaShopperAPI.class);
        }
        return getShoppingModeConfigService;
    }
    public static UpayAPI getUpayAPI(Context context) {
        if (upayAPI == null) {
            upayAPI = getRetrofitBuilder(context).create(UpayAPI.class);
        }
        return upayAPI;
    }


}
