package com.ultramega.shop.activity.shoppingmode;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.LoginActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ShoppingModeConfig;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.GetShoppingModeConfigResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingModeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView imgWholesale;
    private ImageView imgRetail;

    private static final String Theme_Current = "AppliedTheme";
    private UltramegaShopUtilities mdb;

    private MaterialDialog mLogoutConsumerDialog;
    private MaterialDialog mLogoutWholesalerDialog;

    private String imei = "";
    private String usertype = "";
    private String authcode = "";
    private String sessionid = "";

    private List<ShoppingModeConfig> shoppingModeConfigList;

    private TextView txvWholesaleConfigDescription;
    private TextView txvRetailConfigDescription;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isFirstLoad = true;

    int count=0;
    private MaterialDialog mMaterialDialogError = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mode);
        try {

            if (getIntent().getBooleanExtra("islogin", false)) {
                LoginActivity.start(getViewContext());
            }
            init();
            initData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        openConfirmQuitDialog("Are you sure you want to quit?");
    }

    private void init() {
        imgWholesale = (ImageView) findViewById(R.id.imgWholesale);
        imgRetail = (ImageView) findViewById(R.id.imgRetail);
        txvWholesaleConfigDescription = (TextView) findViewById(R.id.txvWholesaleConfigDescription);
        txvRetailConfigDescription = (TextView) findViewById(R.id.txvRetailConfigDescription);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(true);

        imgWholesale.setOnClickListener(null);
        animateImageView(imgWholesale);
        txvWholesaleConfigDescription.setVisibility(View.VISIBLE);

        imgRetail.setOnClickListener(null);
        animateImageView(imgRetail);
        txvRetailConfigDescription.setVisibility(View.VISIBLE);
    }

    private void initData() {
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        sessionid = UserPreferenceManager.getSession(getViewContext());
        shoppingModeConfigList = new ArrayList<>();

        getSession();
    }

    public static void start(Context context, boolean isLogin) {
        Intent intent = new Intent(context, ShoppingModeActivity.class);
        intent.putExtra("islogin", isLogin);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mConfigListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            switch (v.getId()) {
                case R.id.imgWholesale: {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);

                    UserPreferenceManager.clearPreference(getViewContext());
                    UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");

                    if (mdb != null) {

                        if (mdb.getWholesalerID() != null) {

//                        !mdb.getWholesalerID().equals(".")

                            //set up login to true
                            UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);

                            Bundle args = new Bundle();
                            args.putBoolean("isShow", true);
                            MainActivity.start(getViewContext(), 0, args);

                        } else {

                            //set up login to false
                            UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);

                            LoginActivity.start(getViewContext());

                        }
                    }

                    // MainActivity.switchMode(getViewContext(), 0);

                    break;
                }
                case R.id.imgRetail: {


                    //truncate tables
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);

                    UserPreferenceManager.clearPreference(getViewContext());
                    UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");

                    if (mdb != null) {
                        if (mdb.getConsumerID() != null) {
                            //set up login to true
                            sessionid = UserPreferenceManager.getSession(getViewContext());
                            UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
                        } else {
                            //set up login to true
                            UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
                        }
                    }
                    Bundle args = new Bundle();
                    args.putBoolean("isShow", true);
                    MainActivity.start(getViewContext(), 0, args);

                    break;
                }
            }
        }
    };

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imgWholesale: {
//
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
//
//                UserPreferenceManager.clearPreference(getViewContext());
//                UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");
//
//                if (mdb != null) {
//
//                    if (mdb.getWholesalerID() != null) {
//
////                        !mdb.getWholesalerID().equals(".")
//
//                        //set up login to true
//                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
//
//                        Bundle args = new Bundle();
//                        args.putBoolean("isShow", true);
//                        MainActivity.start(getViewContext(), 0, args);
//
//                    } else {
//
//                        //set up login to false
//                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
//
//                        LoginActivity.start(getViewContext());
//
//                    }
//                }
//
//                // MainActivity.switchMode(getViewContext(), 0);
//
//                break;
//            }
//            case R.id.imgRetail: {
//
//                //truncate tables
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_ITEM_SKUS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_REGULAR);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES_ITEMS_DAILYFINDS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_SUPPLIER);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_PROMOPTS);
//                mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);
//
//                UserPreferenceManager.clearPreference(getViewContext());
//                UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");
//
//                if (mdb != null) {
//                    if (mdb.getConsumerID() != null) {
//                        //set up login to true
//                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
//                    } else {
//                        //set up login to true
//                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
//                    }
//                }
//
//                Bundle args = new Bundle();
//                args.putBoolean("isShow", true);
//                MainActivity.start(getViewContext(), 0, args);
//
//                break;
//            }
//        }
//    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //api session
            createSession(callsession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + sessionid);
                    getShoppingModeConfig(getShoppingModeConfigSession);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showError(response.body().getMessage());
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getShoppingModeConfig(Callback<GetShoppingModeConfigResponse> getShoppingModeConfigCallback) {
        Call<GetShoppingModeConfigResponse> getshoppingmode = RetrofitBuild.getShoppingModeConfigService(getViewContext())
                .getShoppingModeConfigCall(imei,
                        authcode,
                        sessionid,
                        RetrofitBuild.appversion,
                        RetrofitBuild.devicetype);
        getshoppingmode.enqueue(getShoppingModeConfigCallback);
    }

    private final Callback<GetShoppingModeConfigResponse> getShoppingModeConfigSession = new Callback<GetShoppingModeConfigResponse>() {

        @Override
        public void onResponse(Call<GetShoppingModeConfigResponse> call, Response<GetShoppingModeConfigResponse> response) {
            ResponseBody errorBody = response.errorBody();

            mSwipeRefreshLayout.setRefreshing(false);

            if (errorBody == null) {

                if (response.body().getStatus().equals("000")) {

                    shoppingModeConfigList = response.body().getShoppingModeConfigList();

                    if (shoppingModeConfigList.size() > 0 && shoppingModeConfigList.size() <= 2) {

                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ULTRAMEGA_SERVICE_CONFIG);

                        for (ShoppingModeConfig shoppingModeConfig : shoppingModeConfigList) {
                            mdb.insertServiceConfig(shoppingModeConfig);
                        }

                        isFirstLoad = false;
                        ShoppingModeConfig shoppingModeConfigWholesaler = mdb.getShoppingModeConfig("B2B");
                        if (!shoppingModeConfigWholesaler.getStatus().equals("ACTIVE")) {
                            imgWholesale.setOnClickListener(null);
                            animateImageView(imgWholesale);
                            txvWholesaleConfigDescription.setVisibility(View.VISIBLE);
                            setConfigDescription(txvWholesaleConfigDescription, shoppingModeConfigWholesaler.getConfigDescription());
                        } else {
                            imgWholesale.setOnClickListener(mConfigListener);
                            txvWholesaleConfigDescription.setVisibility(View.GONE);
                            imgWholesale.clearColorFilter();
                        }

                        ShoppingModeConfig shoppingModeConfigRetail = mdb.getShoppingModeConfig("B2C");
                        if (!shoppingModeConfigRetail.getStatus().equals("ACTIVE")) {
                            imgRetail.setOnClickListener(null);
                            animateImageView(imgRetail);
                            txvRetailConfigDescription.setVisibility(View.VISIBLE);
                            setConfigDescription(txvRetailConfigDescription, shoppingModeConfigRetail.getConfigDescription());
                        } else {
                            imgRetail.setOnClickListener(mConfigListener);
                            txvRetailConfigDescription.setVisibility(View.GONE);
                            imgRetail.clearColorFilter();
                        }

                    } else {
                        showError("Service Config is not set. Please contact Administrator.");
                        imgWholesale.setOnClickListener(null);
                        imgRetail.setOnClickListener(null);
                    }


                }else if(response.body().getStatus().equals("004")){
                    if (mMaterialDialogError == null) {
                        mMaterialDialogError = new MaterialDialog.Builder(getViewContext())
                                .content(response.body().getMessage())
                                .cancelable(false)
                                .positiveText("OK")
                                .contentColorRes(R.color.color_2C2C2C)
                                .positiveColorRes(R.color.colorAccent)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        try {
                                            Intent viewIntent =
                                                    new Intent("android.intent.action.VIEW",
                                                            Uri.parse("https://play.google.com/store/apps/details?id=com.ultramega.shop"));
                                            startActivity(viewIntent);
                                            mMaterialDialogError = null;
                                        }catch(Exception e) {
                                            Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
                                                    Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .show();
                    }
                }else {
                    openErrorDialog(response.body().getMessage());
                }

            }
        }

        @Override
        public void onFailure(Call<GetShoppingModeConfigResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            openErrorDialog("Something went wrong with the server.");
        }
    };

    public void animateImageView(final ImageView v) {

        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(ContextCompat.getColor(getViewContext(), R.color.color_D8000000), mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
                    v.setColorFilter(null);
                }
            }
        });

        if (!isFirstLoad) {
            colorAnim.setDuration(500);
        }

//        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
//        colorAnim.setRepeatCount(-1);
        colorAnim.start();

    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void setConfigDescription(final TextView txv, final String desc) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //===========================================
                //check & execute if activity is still active
                //===========================================
                txv.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", desc));

            }
        }, 550);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (mdb != null) {
            mdb.truncateTable(UltramegaShopUtilities.TABLE_ULTRAMEGA_SERVICE_CONFIG);
        }
        txvRetailConfigDescription.setVisibility(View.GONE);
        txvWholesaleConfigDescription.setVisibility(View.GONE);
        getSession();
    }
}
