package com.ultramega.shop.activity.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrderPayments;
import com.ultramega.shop.pojo.PesoPay;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.CheckCardPaymentStatusResponse;
import com.ultramega.shop.responses.GenericResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;

import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ban_Lenovo on 9/18/2017.
 */

public class PesoPayWebViewActivity extends BaseActivity {

    public static final String KEY_PESOPAY_OBJ = "pesopay_obj";
    public static final String KEY_PAYMENT_TXN_NO = "paymenttxnno";
    public static final String KEY_URL = "url";
    public static final String KEY_AMOUNT = "amount";

    private ProgressBar progressBar;
    private WebView webView;
    private PesoPay mPesoPay;
    private String mTxnNo;
    private String mUrl = "";
    private String mAmount;
    private String customerID;
    private String mobileno;
    private String userID;

    private boolean isCancel = false;

    private UltramegaShopUtilities mDbUtils;

    private boolean isStatusChecked = false;
    private int totaldelaytime = 10000;
    private int currentdelaytime = 0;

    private String imei = "";
    private String authcode = "";
    private String sessionid = "";
    private String customerid = "";
    private String usertype = "";
    private String userid = "";

    //ANIMATED DIALOG
    private MaterialDialog mLoaderDialog;
    private TextView mLoaderDialogMessage;
    private TextView mLoaderDialogTitle;
    private ImageView mLoaderDialogImage;
    private TextView mLoaderDialogClose;
    private TextView mLoaderDialogRetry;
    private RelativeLayout buttonLayout;
    //PAYMENT DETAILS
    private TextView txvTxnId;
    private TextView txvTotalAmount;
    private TextView txvDeliveryCharge;
    private TextView txvAmountPaid;
    private TextView txvPaymentOption;
    private LinearLayout paymentDetailsLayout;

    private boolean isOrder = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peso_pay_web_view);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbUtils = new UltramegaShopUtilities(getViewContext());
        mPesoPay = getIntent().getParcelableExtra(KEY_PESOPAY_OBJ);
        mTxnNo = getIntent().getStringExtra(KEY_PAYMENT_TXN_NO);
        mUrl = getIntent().getStringExtra(KEY_URL);
        mAmount = getIntent().getStringExtra(KEY_AMOUNT);
        isOrder = getIntent().getBooleanExtra("isorder", false);
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mDbUtils.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mDbUtils.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
        }

//        mSmoothProgressBar = (SmoothProgressBar) findViewById(R.id.mSmoothProgressBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webView);
//        webView = new WebView(this);
        WebSettings ws = webView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        //setContentView(webView);
        final String postData =
                "merchantId=" + mPesoPay.getMerchantID() +
                        "&amount=" + mAmount +
                        "&orderRef=" + "OR".concat(mTxnNo) +
                        "&currCode=" + mPesoPay.getCurrencyCode() +
                        "&mpsMode=NIL" +
                        "&successUrl=" + mPesoPay.getSuccessURL() +
                        "&failUrl=" + mPesoPay.getFailedURL() +
                        "&cancelUrl=" + mPesoPay.getCancelledURL() +
                        "&payType=" + mPesoPay.getPaymentType() +
                        "&lang=" + mPesoPay.getLanguage() +
                        "&payMethod=" + mPesoPay.getPaymentMethod() +
                        "&secureHash=" + CommonFunctions.getSha1Hex(
                        mPesoPay.getMerchantID() + "|" +
                                "OR".concat(mTxnNo) + "|" +
                                mPesoPay.getCurrencyCode() + "|" +
                                mAmount + "|" +
                                mPesoPay.getPaymentType() + "|" +
                                mPesoPay.getSecretKey());

        Log.d("antonhttp", "postData: \n" + postData);

        webView.postUrl(mPesoPay.getPesoPayURL(), postData.getBytes());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(20);
//                mSmoothProgressBar.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.postUrl(url, getBytes(postData, "base64"));
                //webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                progressBar.setVisibility(View.GONE);
//                mSmoothProgressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
                mUrl = url;

                Log.d("antonhttp", "onPageFinished: " + mUrl);

                if (mUrl.contains("payForm2")) {

                    createSessionPesoPayWebView();

                } else if (mUrl.contains("success.asp")) {

                    if (isOrder) {
                        //check transaction status here
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                currentdelaytime = currentdelaytime + 1000;
                                isStatusChecked = true;
                                getSession();
                            }
                        }, 1000);
                    }

                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                mSmoothProgressBar.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.postUrl(url, getBytes(postData, "base64"));
//                return false;
//            }
//        });


        //SETUP LOADER DIALOG
        setupLoaderDialog();

    }

    private void setupLoaderDialog() {
        mLoaderDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(false)
                .customView(R.layout.dialog_custom_animated, false)
                .build();

        View view = mLoaderDialog.getCustomView();
        if (view != null) {
            mLoaderDialogMessage = (TextView) view.findViewById(R.id.mLoaderDialogMessage);
            mLoaderDialogTitle = (TextView) view.findViewById(R.id.mLoaderDialogTitle);
            mLoaderDialogImage = (ImageView) view.findViewById(R.id.mLoaderDialogImage);
            mLoaderDialogClose = (TextView) view.findViewById(R.id.mLoaderDialogClose);
            mLoaderDialogClose.setOnClickListener(this);
            mLoaderDialogRetry = (TextView) view.findViewById(R.id.mLoaderDialogRetry);
            mLoaderDialogRetry.setVisibility(View.GONE);
            mLoaderDialogRetry.setOnClickListener(this);
            buttonLayout = (RelativeLayout) view.findViewById(R.id.buttonLayout);
            buttonLayout.setVisibility(View.GONE);
            //PAYMENT DETAILS
            txvTxnId = (TextView) view.findViewById(R.id.txvtxnid);
            txvTotalAmount = (TextView) view.findViewById(R.id.txvtotalamount);
            txvAmountPaid = (TextView) view.findViewById(R.id.txvAmountPaid);
            txvDeliveryCharge = (TextView) view.findViewById(R.id.txvDeliveryCharge);
            txvPaymentOption = (TextView) view.findViewById(R.id.txvPaymentOption);
            paymentDetailsLayout = (LinearLayout) view.findViewById(R.id.paymentDetailsLayout);

            mLoaderDialogTitle.setText("Processing...");

            Glide.with(getViewContext())
                    .load(R.drawable.loadergif3)
                    .into(mLoaderDialogImage);
        }
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mLoaderDialog.show();

            //API CALL
            createSession(callSession);
        } else {
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callSession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
                    if (isStatusChecked) {
                        mLoaderDialogMessage.setText("Checking status, Please wait...");
                        checkCardPaymentStatus(checkCardPaymentStatusSession);
                    }
                } else {
                    mLoaderDialog.hide();
                    openErrorDialog(response.body().getMessage());
                }
            } else {
                mLoaderDialog.hide();
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mLoaderDialog.hide();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    private void checkCardPaymentStatus(Callback<CheckCardPaymentStatusResponse> checkCardPaymentStatusCallback) {
        Call<CheckCardPaymentStatusResponse> checkcardpaymentstatus = RetrofitBuild.checkCardPaymentStatusService(getViewContext())
                .checkCardPaymentStatusCall(imei,
                        authcode,
                        sessionid,
                        customerid,
                        usertype,
                        userid,
                        mTxnNo);
        checkcardpaymentstatus.enqueue(checkCardPaymentStatusCallback);
    }

    private final Callback<CheckCardPaymentStatusResponse> checkCardPaymentStatusSession = new Callback<CheckCardPaymentStatusResponse>() {

        @Override
        public void onResponse(Call<CheckCardPaymentStatusResponse> call, Response<CheckCardPaymentStatusResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {

                switch (response.body().getStatus()) {
                    case "000": {

                        OrderPayments orderPayments = response.body().getOrderPayments();
                        if (orderPayments.getPaymentStatus().equals("FAILED")) {
                            paymentDetailsLayout.setVisibility(View.GONE);
                            mLoaderDialogTitle.setText("FAILED TRANSACTION");
                        } else {
                            paymentDetailsLayout.setVisibility(View.VISIBLE);
                            mLoaderDialogTitle.setText("SUCCESSFUL TRANSACTION");

                            txvPaymentOption.setText(orderPayments.getPaymentOption());
                            txvDeliveryCharge.setText(CommonFunctions.currencyFormatter(String.valueOf(orderPayments.getDeliveryCharge())));
                            txvTxnId.setText(orderPayments.getPaymentTxnNo());
                            txvTotalAmount.setText(CommonFunctions.currencyFormatter(String.valueOf(orderPayments.getTotalItemAmount())));
                            txvAmountPaid.setText(CommonFunctions.currencyFormatter(String.valueOf(orderPayments.getAmountPaid())));
                        }

                        buttonLayout.setVisibility(View.VISIBLE);
                        mLoaderDialogRetry.setVisibility(View.GONE);
                        mLoaderDialogMessage.setText(response.body().getMessage());
                        mLoaderDialogImage.setVisibility(View.GONE);
                        mLoaderDialogClose.setVisibility(View.VISIBLE);

                        break;
                    }
                    case "006": {

                        paymentDetailsLayout.setVisibility(View.GONE);

                        if (currentdelaytime < totaldelaytime) {
                            //check transaction status here
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    currentdelaytime = currentdelaytime + 1000;
                                    checkCardPaymentStatus(checkCardPaymentStatusSession);
                                }
                            }, 1000);
                        } else {
                            buttonLayout.setVisibility(View.VISIBLE);
                            mLoaderDialogRetry.setVisibility(View.GONE);
                            mLoaderDialogTitle.setText("TRANSACTION ON PROCESS");
                            mLoaderDialogMessage.setText(response.body().getMessage());
                            mLoaderDialogImage.setVisibility(View.GONE);
                            mLoaderDialogClose.setVisibility(View.VISIBLE);
                        }

                        break;
                    }
                    default: {
                        paymentDetailsLayout.setVisibility(View.GONE);
                        mLoaderDialog.dismiss();
                        openErrorDialog(response.body().getMessage());
                        break;
                    }
                }

            } else {
                mLoaderDialog.dismiss();
                openErrorDialog("Something went wrong with the server.");
            }
        }

        @Override
        public void onFailure(Call<CheckCardPaymentStatusResponse> call, Throwable t) {
            mLoaderDialog.dismiss();
            openErrorDialog("Something went wrong with the server.");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_peso_pay, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUrl.contains("payComp") || mUrl.contains("ultramega.godvapps")) {
            menu.findItem(R.id.pp_cancel).setVisible(false);
            menu.findItem(R.id.pp_done).setVisible(true);
        } else {
            menu.findItem(R.id.pp_cancel).setVisible(true);
            menu.findItem(R.id.pp_done).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public static byte[] getBytes(final String data, final String charset) {

        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

    public static void start(Context context, PesoPay pesoPay, String txnNo, String url, String amount, boolean isOrder) {

        Log.d("antonhttp", "pesoPay RECEIVED: " + new Gson().toJson(pesoPay));

        Intent intent = new Intent(context, PesoPayWebViewActivity.class);
        intent.putExtra(KEY_PESOPAY_OBJ, pesoPay);
        intent.putExtra(KEY_PAYMENT_TXN_NO, txnNo);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_AMOUNT, amount);
        intent.putExtra("isorder", isOrder);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            showCancelConfirmationDialog();
        else if (item.getItemId() == R.id.pp_cancel) {
            showCancelConfirmationDialog();
        } else if (item.getItemId() == R.id.pp_done) {

            if (isOrder) {

                if (mDbUtils != null) {
                    CommonFunctions.hasNewOrderUpdate = true;
                    mDbUtils.truncateTable(mDbUtils.TABLE_ORDERS_QUEUE);
                }

            }

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mUrl.contains("payComp") && !mUrl.contains("ultramega.godvapps"))
            showCancelConfirmationDialog();
        else
            super.onBackPressed();
    }

    private void showCancelConfirmationDialog() {
        new MaterialDialog.Builder(getViewContext())
                .content("Are you sure you want to CANCEL reloading thru card?")
                .positiveText("Yes")
                .negativeText("No")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                        isCancel = true;
                        createSessionPesoPayWebView();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        isCancel = false;
                    }
                })
                .show();
    }

    private void createSessionPesoPayWebView() {

        if (getCurrentUserType(getViewContext()).equals("CONSUMER")) {
            userID = mDbUtils.getConsumerProfile().getUserID();
            mobileno = mDbUtils.getConsumerProfile().getCountryCode().split(":::")[0].
                    concat(mDbUtils.getConsumerProfile().getMobileNo());
            customerID = mDbUtils.getConsumerProfile().getConsumerID();
        } else {
            customerID = mDbUtils.getWholesalerProfile().getUserID();
            userID = "63".concat(mDbUtils.getWholesalerProfile().getMobileNo());
            customerID = mDbUtils.getWholesalerProfile().getWholesalerID();
        }

        Call<GetSessionResponse> call = RetrofitBuild.commonApiService(getViewContext())
                .fetchRegisteredSessionCall(
                        CommonFunctions.getIMEI(getViewContext()),
                        userID,
                        getCurrentUserType(getViewContext()),
                        mobileno
                );

        call.enqueue(sessionCallbackPesoPayWebView);
    }

    private Callback<GetSessionResponse> sessionCallbackPesoPayWebView = new Callback<GetSessionResponse>() {
        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();

                    if (isCancel) {
                        updateStatusCancell();
                    } else {
                        updateStatus();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {

        }
    };

    private void updateStatus() {
        Call<GenericResponse> call = RetrofitBuild.commonApiService(getViewContext())
                .updateStatusReloadWalletViaPesoPay(
                        CommonFunctions.getIMEI(getViewContext()),
                        userID,
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + userID + sessionid),
                        sessionid,
                        "OR".concat(mTxnNo),
                        customerID,
                        "FORAPPROVAL"
                );

        call.enqueue(updateStatusCallback);
    }

    private void updateStatusCancell() {
        Call<GenericResponse> call = RetrofitBuild.commonApiService(getViewContext())
                .updateStatusReloadWalletViaPesoPay(
                        CommonFunctions.getIMEI(getViewContext()),
                        userID,
                        CommonFunctions.getSha1Hex(CommonFunctions.getIMEI(getViewContext()) + userID + sessionid),
                        sessionid,
                        "OR".concat(mTxnNo),
                        customerID,
                        "CANCELLED"
                );

        call.enqueue(updateStatusCallback);
    }

    private Callback<GenericResponse> updateStatusCallback = new Callback<GenericResponse>() {
        @Override
        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
            if (response.errorBody() == null) {
                if (response.body().getStatus().equals("000")) {
                    if (isCancel)
                        isCancel = false;
                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                }
            }
        }

        @Override
        public void onFailure(Call<GenericResponse> call, Throwable t) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLoaderDialogClose: {
                mLoaderDialog.dismiss();
                break;
            }
            case R.id.mLoaderDialogRetry: {

                break;
            }
        }
    }
}
