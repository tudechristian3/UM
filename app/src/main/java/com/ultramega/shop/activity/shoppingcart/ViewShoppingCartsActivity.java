package com.ultramega.shop.activity.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.CheckoutProductsActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.PickUpActivity;
import com.ultramega.shop.activity.notification.NotificationActivity;
import com.ultramega.shop.adapter.myshoppingcart.MyShoppingCartRecyclerAdapter;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.decoration.DividerItemDecoration;
import com.ultramega.shop.enums.GenericEnums;
import com.ultramega.shop.kotlinpaymaya.PaymayaRedirectionActivity;
import com.ultramega.shop.pojo.BankReference;
import com.ultramega.shop.pojo.ConsumerCharge;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ConsumerWallet;
import com.ultramega.shop.pojo.OrderPayments;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.CalculateOrderChargeResponse;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.responses.GetBankReferenceResponse;
import com.ultramega.shop.responses.GetConsumerWalletResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewShoppingCartsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private UltramegaShopUtilities mdb;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyShoppingCartRecyclerAdapter mShoppingCartAdapter;

    private TextView txv_my_shopping_cart_product_quantity, txv_my_shopping_cart_product_total;

    private LinearLayout confirm_layout;



    LinearLayout linearCash, linearPaymaya, linearUpay, linearWallet, linearCard;

    private TextView text_empty;
    private ImageView image_empty;
    private LinearLayout layout_empty;



    private String usertype = "";

    private NestedScrollView nested_scroll;
    private boolean isloadmore;
    private boolean isbottomscroll;
    private int limit;


    private ShoppingCartSummary shoppingCartSummary;

    private String customerid = "";
    private String userid = "";

    private LinearLayout mSmoothProgressBar;

    private LinearLayout walletLayout;
    private TextView walletbalancelabel;
    private TextView walletbalance;
    private ImageView refreshwallet;
    private ImageView right_icon;

    //NOTIF BADGE
    private TextView txvNotificationItemCount;
    private String mNotificaitonItemCount = "";

    private static ViewShoppingCartsActivity instance;

    private Button btnCheckout;
    private MaterialDialog mPaymentOptionsDialog;
    private MaterialDialog mPickUpDialog, mDeliverDialog;
    private String paymenttype;
    private int deliveryOption;

    private Button btnDeliver, btnPickup;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //==================
        //setup badge layout
        //==================
        final MenuItem menuNotif = menu.findItem(R.id.action_notification);
        View actionViewNotif = MenuItemCompat.getActionView(menuNotif);
        txvNotificationItemCount = (TextView) actionViewNotif.findViewById(R.id.notification_badge);

        //======================================
        //apply badge count on notification icon
        //======================================
        setUpBadge();

        actionViewNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuNotif);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_notification: {
                NotificationActivity.start(getViewContext());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //=========================================
    // APPLY BADGE COUNT ON CART ICON
    //=========================================
    public void setUpBadge() {
        if (mdb != null) {
            setUpBadgeNotifiation();
        }
    }

    private void setUpBadgeNotifiation() {
        if (txvNotificationItemCount != null) {
            int count = UserPreferenceManager.getIntPreference(getViewContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT);
            if (count == 0) {
                if (txvNotificationItemCount.getVisibility() != View.GONE) {
                    txvNotificationItemCount.setVisibility(View.GONE);
                }
            } else {
                mNotificaitonItemCount = String.valueOf(count);
                txvNotificationItemCount.setText(mNotificaitonItemCount);
                if (txvNotificationItemCount.getVisibility() != View.VISIBLE) {
                    txvNotificationItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem notification = menu.findItem(R.id.action_notification);
        if (customerid != null) {
            notification.setVisible(true);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_carts);
        //getValues();
        //validateQRPartialV2();
        setUpToolBar();
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Cart"));
        sessionid = UserPreferenceManager.getSession(getViewContext());

        try {

            init();

            initData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        //WALLET LAYOUT
        walletLayout = (LinearLayout) findViewById(R.id.walletLayout);
        walletbalancelabel = (TextView) findViewById(R.id.walletbalancelabel);
        walletbalance = (TextView) findViewById(R.id.walletbalance);
        refreshwallet = (ImageView) findViewById(R.id.refreshwallet);
        right_icon = (ImageView) findViewById(R.id.right_icon);
        walletbalancelabel.setOnClickListener(this);
        walletbalance.setOnClickListener(this);
        refreshwallet.setOnClickListener(this);
        right_icon.setOnClickListener(this);

        mSmoothProgressBar = (LinearLayout) findViewById(R.id.mSmoothProgressBar);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        text_empty = (TextView) findViewById(R.id.text_empty);
        image_empty = (ImageView) findViewById(R.id.image_empty);
        image_empty.setImageDrawable(ContextCompat.getDrawable(getViewContext(), R.drawable.empty_cart));

        confirm_layout = (LinearLayout) findViewById(R.id.confirm_layout);
        confirm_layout.setVisibility(View.GONE);
        txv_my_shopping_cart_product_quantity = (TextView) findViewById(R.id.my_shopping_cart_product_quantity);
        txv_my_shopping_cart_product_total = (TextView) findViewById(R.id.my_shopping_cart_product_total);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_my_shopping_cart);

        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);

    }

    private void initData() {
        //initialize empty data
        mdb = new UltramegaShopUtilities(getViewContext());
        isbottomscroll = false;
        isloadmore = true;
        limit = 0;
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        instance = this;

        // initialize with empty data
        walletbalancelabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "BAL:"));
        walletbalance.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())).concat(" PHP")));

        if (usertype.equals("CONSUMER")) {
            ConsumerProfile itemProf = mdb.getConsumerProfile();
            customerid = itemProf.getConsumerID();
            userid = itemProf.getUserID();
            walletLayout.setBackgroundResource(R.color.color_2196F3);
            btnCheckout.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_034b9b));
        } else if (usertype.equals("WHOLESALER")) {
            WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();
            userid = wholesalerProfile.getUserID();
            walletLayout.setVisibility(View.GONE);
            btnCheckout.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_EB1C24));
        }

        if (customerid == null) {
            walletLayout.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        recyclerView.setNestedScrollingEnabled(false);
        mShoppingCartAdapter = new MyShoppingCartRecyclerAdapter(getViewContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getViewContext(), R.drawable.recycler_divider)));
        recyclerView.setAdapter(mShoppingCartAdapter);

        setShoppingSummaryUI();

        if (customerid != null) {

            getSession();

        }

        updateFirsLoadList(mdb.getAllShoppingCartsQueue());

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    //Log.d("antonhttp", "Scroll DOWN");
                    mSwipeRefreshLayout.setEnabled(false);
                }
                if (scrollY < oldScrollY) {
                    //Log.d("antonhttp", "Scroll UP");
                    mSwipeRefreshLayout.setEnabled(false);
                }

                if (scrollY == 0) {
                    //Log.d("antonhttp", "TOP SCROLL");
                    mSwipeRefreshLayout.setEnabled(true);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    mSwipeRefreshLayout.setEnabled(false);
                    if (customerid != null) {
                        isbottomscroll = true;
                        if (isloadmore) {
                            limit = limit + 30;
                            //limit = 0;
                            getSession();
                        }
                    }
                }
            }
        });
    }

    public static ViewShoppingCartsActivity getInstance() {
        return instance;
    }

    public void updateFirsLoadList(List<ShoppingCartsQueue> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            confirm_layout.setVisibility(View.VISIBLE);
            if (mShoppingCartAdapter != null) {
                mShoppingCartAdapter.setCartData(data);
            }
        } else {
            if (mShoppingCartAdapter != null) {
                mShoppingCartAdapter.clearCartData();
            }
            confirm_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
        }
    }

    public void updateList(List<ShoppingCartsQueue> data) {
        if (data.size() > 0) {
            layout_empty.setVisibility(View.GONE);
            confirm_layout.setVisibility(View.VISIBLE);
            if (mShoppingCartAdapter != null) {
                mShoppingCartAdapter.setCartData(data);
            }
        } else {
            if (mShoppingCartAdapter != null) {
                mShoppingCartAdapter.clearCartData();
            }
            confirm_layout.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
            text_empty.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Nothing in here"));
        }
    }

    private void getValues() {
        mdb = new UltramegaShopUtilities(getViewContext());
        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        userid = itemProf.getUserID();
        customerid = mdb.getConsumerID();
        userid = itemProf.getUserID();
        sessionid = UserPreferenceManager.getSession(getViewContext());
        //sessionid = UserPreferenceManager.getSession(getViewContext());
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(callsession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    private void getWalletSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            createSession(getWalletSession);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    }

    public void setShoppingSummaryUI() {

        if (mdb.getShoppingCartSummary(usertype) != null) {

            shoppingCartSummary = mdb.getShoppingCartSummary(usertype);

            txv_my_shopping_cart_product_total.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.currencyFormatter(String.valueOf(shoppingCartSummary.getTotalAmount()))));

            if (usertype.equals("CONSUMER")) {

                findViewById(R.id.totalBulkLayout).setVisibility(View.GONE);
                findViewById(R.id.totalNonBulkLayout).setVisibility(View.GONE);
                findViewById(R.id.totalQuantityLayout).setVisibility(View.VISIBLE);

                txv_my_shopping_cart_product_quantity.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(shoppingCartSummary.getQuantity()))));

            } else {

                findViewById(R.id.totalBulkLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.totalNonBulkLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.totalQuantityLayout).setVisibility(View.GONE);

                ((TextView) findViewById(R.id.txvTotalBulk)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(shoppingCartSummary.getTotalBulk()))));
                ((TextView) findViewById(R.id.txvTotalNonBulk)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", CommonFunctions.commaFormatter(String.valueOf(shoppingCartSummary.getTotalNonBulk()))));

            }

        }

    }

    private final Callback<GetSessionResponse> getWalletSession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    if (usertype.equals("CONSUMER")) {
                        getWallet();
                    }
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    if (usertype.equals("CONSUMER")) {
                        getWallet();
                        getBankReference();
                    }
                    getShoppingCart(cartCallback);
                } else {
                    mSmoothProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    showError(response.body().getMessage());
                }
            } else {
                mSmoothProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                showError(getString(R.string.generic_internet_error_message));
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mdb != null)
            walletbalance.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())).concat(" PHP")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckout: {
                try {
                    if (customerid == null) {

                        openLoginDialog("You are required to login to proceed with your orders");

                    } else {
                        if (mdb.getAllShoppingCartsQueue().size() > 0) {

                            if (usertype.equals("CONSUMER")) {

                                openDeliveryOptionDialog();

                            } else if (usertype.equals("WHOLESALER")) {

                                CheckoutProductsActivity.start(getViewContext(), 2);

                            }

                        } else {
                            openErrorDialog("Please fill your cart to checkout");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.depositwallet:
            case R.id.refreshwallet: {
                getWalletSession();
                break;
            }
            case R.id.consumerwallet:
            case R.id.right_icon:
            case R.id.walletbalance:
            case R.id.walletbalancelabel: {
                MainActivity.start(getViewContext(), 5);
                break;
            }

            case R.id.cashOption:
                paymentOption(GenericEnums.CASH);
                break;
            case R.id.paymayaOption:
                paymentOption(GenericEnums.CARD);
//                int which = 1;
//                deliveryOption = which;
//                linearCash.setVisibility(View.GONE);
//                mPickUpDialog.dismiss();
//                linearCard.setVisibility(View.VISIBLE);
//                linearUpay.setVisibility(View.VISIBLE);
//                linearWallet.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplication(), "paymayaOption", Toast.LENGTH_SHORT).show();
                break;
            case R.id.upayOption:
                paymentOption(GenericEnums.UPAY);
                break;

        }
    }

    private void openDeliveryOptionDialog() {
//        new MaterialDialog.Builder(getViewContext())
//                .title("Choose your delivery option")
//                .items(R.array.str_action_delivery_option)
//                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//
//                        deliveryOption = which;
//
//                        if (which == 0) {
//
//                            if (isAirplaneModeOn(getViewContext())) {
//                                showError("Airplane mode is enabled. Please disable Airplane mode and enable GPS or Internet to proceed.");
//                            } else {
//                                if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
//                                    if (isGPSModeOn()) {
//                                         dialog.dismiss();
//                                         setUpPaymentOptionsDialog();
//                                    } else {
//                                        showGPSDisabledAlertToUser();
//                                    }
//                                } else {
//                                    showError(getString(R.string.generic_internet_error_message));
//                                }
//                            }
//
//                        } else {
//                            setUpPaymentOptionsDialog();
//                        }
//                        return true;
//                    }
//                })
//                .show();


        mPickUpDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_delivery_options, false)
                .build();

        View view = mPickUpDialog.getCustomView();
        int which = 1;
        int which2 = 0;



        view.findViewById(R.id.btnPickUpOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                    view1.setBackgroundResource(R.color.color_0060B0);
                deliveryOption = which2;
                    //mPickUpDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPickUpDialog.dismiss();
                    }
                }, 100);
                if(which2 == 0){
                    setUpPaymentOptionsDialog();
                }
            }
        });

        view.findViewById(R.id.btnDeliverOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                view2.setBackgroundResource(R.color.color_0060B0);
                deliveryOption = which;

                if(which == 0){
                    if (isAirplaneModeOn(getViewContext())) {
                        showError("Airplane mode is enabled. Please disable Airplane mode and enable GPS or Internet to proceed.");
                    }
                    else{
                        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
                            if (isGPSModeOn()) {
                                setUpPaymentOptionsDialog();
                            }
                            else {
                                showGPSDisabledAlertToUser();
                            }
                        }else{
                            showError(getString(R.string.generic_internet_error_message));
                        }
                    }
                }
                else{
                    setUpPaymentOptionsDialog();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPickUpDialog.dismiss();
                    }
                }, 100);
            }
        });


        mPickUpDialog.show();


    }

    @Override
    public void onRefresh() {
        //api session
        limit = 0;
        isbottomscroll = false;
        isloadmore = true;
        if (mdb != null) {
            if (customerid != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                getSession();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                updateList(mdb.getAllShoppingCartsQueue());
            }
        } else {
            updateList(mdb.getAllShoppingCartsQueue());
        }

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ViewShoppingCartsActivity.class);
        context.startActivity(intent);
    }

    private void getBankReference() {
        Call<GetBankReferenceResponse> call = RetrofitBuild.getBankReferenceService(getViewContext())
                .getBankReferenceCall(
                        imei,
                        CommonFunctions.getSha1Hex(imei + sessionid),
                        sessionid);
        call.enqueue(getBankReference);
    }

    private Callback<GetBankReferenceResponse> getBankReference = new Callback<GetBankReferenceResponse>() {

        @Override
        public void onResponse(Call<GetBankReferenceResponse> call, Response<GetBankReferenceResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_ADMIN_BANK_DETAILS);
                    List<BankReference> bankReferences = response.body().getBankReference();
                    for (BankReference bankreference : bankReferences) {
                        mdb.insertBankReference(bankreference);
                    }
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSmoothProgressBar.setVisibility(View.GONE);
                }
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                mSmoothProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<GetBankReferenceResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    };

    private void validateQRPartialV2() {

        if(CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            LinkedHashMap<String,String> parameters = new LinkedHashMap<>();
            parameters.put("usertype","CONSUMER");
            parameters.put("sessionid",sessionid);
            parameters.put("imei",imei);
            parameters.put("authcode",CommonFunctions.getSha1Hex(imei + sessionid + customerid));
            parameters.put("customerid",customerid);
            parameters.put("userid", userid);


            String checker = new Gson().toJson(parameters);
            Log.d("SPecial", "JSON ipada: "+checker);

            getShoppingCart(cartCallback);

        } else {
            Toast.makeText(getViewContext(), "qwe", Toast.LENGTH_SHORT).show();
        }
    }

    private void getShoppingCart(Callback<FetchShoppingCartsQueueResponse> cartCallback) {
        Call<FetchShoppingCartsQueueResponse> call = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
                .fetchShoppingCartsQueueCall(
                        imei,
                        CommonFunctions.getSha1Hex(imei + userid + sessionid),
                        "0",
                        sessionid,
                        userid,
                        customerid,
                        getCurrentUserType(getViewContext()));
        call.enqueue(cartCallback);
    }

    private Callback<FetchShoppingCartsQueueResponse> cartCallback = new Callback<FetchShoppingCartsQueueResponse>() {
        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            //hideProgressDialog();
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    isloadmore = response.body().getShoppingCartsQueues().size() > 0;
                    if (!isbottomscroll) {
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    }
                    List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
                    for (ShoppingCartsQueue queue : shoppingqueue) {
                        mdb.insertAllShoppingCartsQueue(queue);
                    }
                    updateList(mdb.getAllShoppingCartsQueue());
                } else {
                    showError(response.body().getMessage());
                }
            } else {
                showError(getString(R.string.generic_internet_error_message));
            }
        }


        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            //hideProgressDialog();
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
            showError(getString(R.string.generic_internet_error_message));
        }
    };

    private void getWallet() {
        Call<GetConsumerWalletResponse> call = RetrofitBuild.getConsumerWalletService(getViewContext())
                .getConsumerWalletCall(
                        customerid,
                        userid,
                        CommonFunctions.getSha1Hex(imei + userid + sessionid),
                        imei,
                        sessionid);
        call.enqueue(getWallet);
    }

    //ok na ni
    private Callback<GetConsumerWalletResponse> getWallet = new Callback<GetConsumerWalletResponse>() {

        @Override
        public void onResponse(Call<GetConsumerWalletResponse> call, Response<GetConsumerWalletResponse> response) {
            ResponseBody errorBody = response.errorBody();
            mSmoothProgressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_DELIVERY_CHARGE);
                    List<ConsumerWallet> consumerwallet = response.body().getConsumerWallet();
                    for (ConsumerWallet wallet : consumerwallet) {
                        mdb.insertConsumerWallet(wallet);
                    }
                    List<ConsumerCharge> consumerCharge = response.body().getConsumerCharge();
                    for (ConsumerCharge charge : consumerCharge) {
                        mdb.insertConsumerCharge(charge);
                    }
                    walletbalance.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())).concat(" PHP")));

                } else if (response.body().getStatus().equals("004")) {

                    forceLogoutDialog("Invalid User");

                }
            } else {
                openErrorDialog("Something went wrong with the server. Please contact support.");
            }
        }

        @Override
        public void onFailure(Call<GetConsumerWalletResponse> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSmoothProgressBar.setVisibility(View.GONE);
        }
    };

    public void isCheckoutDisabled() {

        boolean isEnabled = isCheckoutEnable();

        if (isEnabled) {

            if (getCurrentUserType(getViewContext()).equals("CONSUMER")) {
                ((Button) findViewById(R.id.btnCheckout)).setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_034b9b));
            } else if (getCurrentUserType(getViewContext()).equals("WHOLESALER")) {
                ((Button) findViewById(R.id.btnCheckout)).setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_EB1C24));
            }

            ((Button) findViewById(R.id.btnCheckout)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "CHECKOUT"));
        } else {
            ((Button) findViewById(R.id.btnCheckout)).setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.color_A9A9A9));
            ((Button) findViewById(R.id.btnCheckout)).setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "INVALID QUANTITY"));
        }

        findViewById(R.id.btnCheckout).setEnabled(isEnabled);

    }

    private boolean isCheckoutEnable() {
        boolean isValid = true;
        List<ShoppingCartsQueue> mOrderData = mdb.getAllShoppingCartsQueue();

        for (ShoppingCartsQueue order : mOrderData) {
            isValid = isValid && (order.getQuantity() >= order.getMinimumOrderQTY() && order.getQuantity() % order.getIncrementalOrderQTY() == 0);
        }
        return isValid;
    }


    private void setUpPaymentOptionsDialog() {
        mPaymentOptionsDialog = new MaterialDialog.Builder(getViewContext())
                .cancelable(true)
                .customView(R.layout.dialog_payment_options, false)
                .build();

        View view = mPaymentOptionsDialog.getCustomView();

        view.findViewById(R.id.cardOption).setOnClickListener(this);
        view.findViewById(R.id.walletOption).setOnClickListener(this);
        view.findViewById(R.id.cashOption).setOnClickListener(this);
        view.findViewById(R.id.paymayaOption).setOnClickListener(this);
        view.findViewById(R.id.upayOption).setOnClickListener(this);

        TextView txvTitle = view.findViewById(R.id.title);
        TextView txvCardLabel = view.findViewById(R.id.txvCardLabel);
        TextView txvMasterCard = view.findViewById(R.id.txvMasterCard);
        TextView txvVisa = view.findViewById(R.id.txvVisa);
        TextView txvWalletLabel = view.findViewById(R.id.txvWalletLabel);
        //txvWalletBalance = view.findViewById(R.id.txvWalletBalance);

        linearCash =  view.findViewById(R.id.cashOption);
        linearPaymaya =  view.findViewById(R.id.paymayaOption);
        linearUpay =  view.findViewById(R.id.upayOption);
        linearCard = view.findViewById(R.id.cardOption);
        linearWallet = view.findViewById(R.id.walletOption);
        //new payment option


        txvTitle.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "Payment Options"));
        txvCardLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "CREDIT CARD"));
        txvMasterCard.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Master Card"));
        txvVisa.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Visa"));
        txvWalletLabel.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", "WALLET"));

        String balance = "Bal: ".concat(CommonFunctions.currencyFormatter(String.valueOf(mdb.getConsumerTotalWallet())));
        //txvWalletBalance.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", balance));

        mPaymentOptionsDialog.show();
    }
    //payment option
    private void paymentOption(GenericEnums type){
        switch (type){
            case CASH:
                paymenttype = "CASH";
                break;
            case CARD:
                paymenttype = "CARD";
                break;
            case UPAY:
                paymenttype = "UPAY";
                break;
        }
        UserPreferenceManager.saveStringPreference(getViewContext(),UserPreferenceManager.KEY_TEMP_PAYMENTOPTION,paymenttype);
        if(deliveryOption == 0){
            PickUpActivity.start(getViewContext(), 1);
        }
        else{

            new MaterialDialog.Builder(getViewContext())
                    .title("Note")
                    .content("Delivery will be handled by a third party delivery carrier.")
                    .cancelable(true)
                    .negativeText("Proceed")
                    .contentColorRes(R.color.color_2C2C2C)
                    .negativeColorRes(R.color.color_2C2C2C)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PickUpActivity.startDelivery(getViewContext(), 1,"DELIVERY");
                        }
                    })
                    .show();


        }


    }

}
