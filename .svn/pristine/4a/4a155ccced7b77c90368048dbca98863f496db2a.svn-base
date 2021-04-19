package com.ultramega.shop.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.notification.NotificationActivity;
import com.ultramega.shop.activity.shoppingcart.ViewShoppingCartsActivity;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.LoyaltyRewardsFragment;
import com.ultramega.shop.fragment.MyListFragment;
import com.ultramega.shop.fragment.OrdersFragment;
import com.ultramega.shop.fragment.WalletReloadFragment;
import com.ultramega.shop.fragment.shop.CategoriesListFragment;
import com.ultramega.shop.fragment.shop.DailyFindsFragment;
import com.ultramega.shop.fragment.shop.PromosFragment;
import com.ultramega.shop.fragment.shop.SuppliersFragment;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ShoppingCartSummary;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 1;
    private static final String TAG = "MAINACTIVITY";
    private static final String EXTRA_TEXT = "RESTART";
    private String usertype = "";
    private NavigationView navigationView;

    private ShoppingCartSummary shoppingCartSummary;

    private Menu menu;
    private View view;
    private LinearLayout loginsignuplayout;
    private CircleImageView profileimage;
    private TextView profilename;
    private ImageView settingsimage;
    private String authcode = "";
    private String limit = "";
    private String userid = "";
    private String customertype = "";
    private String sessionid = "";
    private String customerid = "";
    private UltramegaShopUtilities mdb;
    private ConsumerProfile itemProf = null;
    ;
    private WholesalerProfile wholesalerProfile;
    private List<ConsumerBehaviourPattern> listConsumerPatterns;



    private boolean isLogin = false;

    //CART BADGE
    private TextView txvCartItemCount;
    private String mCartItemCount = "";

    //NOTIF BADGE
    private TextView txvNotificationItemCount;
    private String mNotificaitonItemCount = "";

    private static MainActivity instance;

    private boolean isSwitched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolBar();

        getValues();

        validateQRPartialV2();

        instance = this;
        listConsumerPatterns = new ArrayList<>();
        mdb = new UltramegaShopUtilities(getViewContext());
        wholesalerProfile = mdb.getWholesalerProfile();
        itemProf = mdb.getConsumerProfile();
        usertype = getCurrentUserType(getViewContext());

        if (usertype.equals("CONSUMER")) {
            listConsumerPatterns = mdb.getConsumerBehaviourPatterns();
        } else if (usertype.equals("WHOLESALER")) {
            listConsumerPatterns = mdb.getWholesalerBehaviourPatterns();
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNavHeaderLayout(UserPreferenceManager.getStringPreference(getViewContext(), Theme_Current), navigationView);

        menu = navigationView.getMenu();
        view = navigationView.getHeaderView(0);
        loginsignuplayout = view.findViewById(R.id.loginsignuplayout);
        profileimage = view.findViewById(R.id.profile_image);
        profilename = view.findViewById(R.id.name);
        settingsimage = view.findViewById(R.id.img_settings);
        settingsimage.setOnClickListener(this);
        profileimage.setOnClickListener(this);

        setUpNavMenu();

        isSwitched = getIntent().getBooleanExtra("isSwitch", false);
        if (isSwitched) {
            LoginActivity.startClearStack(getViewContext());
        }

        int index = getIntent().getIntExtra("index", 0);
        Bundle args = getIntent().getBundleExtra("args");
        displayFrag(index, args);
    }

    public void hideToolBar(boolean isHide) {
        if (!isHide) {
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.toolbar).setVisibility(View.GONE);
        }
    }

    private void setUpNavMenu() {
        menu.findItem(R.id.nav_switch_mode).setVisible(false);

        if (usertype.equals("CONSUMER")) {
            setUpConsumerNavMenu();
        } else if (usertype.equals("WHOLESALER")) {
            setUpWholesalerNavMenu();
        }
        setNavFontFace(menu);
    }

    private void setUpWholesalerNavMenu() {
        WholesalerProfile wholesalerProfile = mdb.getWholesalerProfile();

        if (!wholesalerProfile.getWholesalerID().equals(".") || !wholesalerProfile.getWholesalerID().equals(".")) {
            menu.findItem(R.id.nav_switch_mode).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_shop).setVisible(true);
            menu.findItem(R.id.nav_my_shopping_cart).setVisible(true);
            menu.findItem(R.id.nav_my_orders).setVisible(true);
            menu.findItem(R.id.nav_loyalty_rewards).setVisible(true);
            menu.findItem(R.id.nav_my_list).setVisible(true);
            menu.findItem(R.id.nav_wallet_reload).setVisible(false);

            loginsignuplayout.setVisibility(View.GONE);
            profilename.setVisibility(View.VISIBLE);
            profileimage.setVisibility(View.VISIBLE);
            settingsimage.setVisibility(View.VISIBLE);
            profileimage.setOnClickListener(this);

            Log.d("antonhttp", " WHOLESALER PROFILE PIC: " + wholesalerProfile.getProfilePicURL());

            Glide.with(getViewContext())
                    .load(wholesalerProfile.getProfilePicURL())
                    .error(Glide.with(getViewContext())
                            .load(R.drawable.ic_um_default_products))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(RequestOptions.circleCropTransform()
                            .fitCenter()
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                    .into(profileimage);

            String mname = wholesalerProfile.getMiddleName().trim().length() > 1 ? wholesalerProfile.getMiddleName() : "";
            String mProfileName = wholesalerProfile.getFirstName() + " " + mname + " " + wholesalerProfile.getLastName();
            profilename.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", mProfileName.toUpperCase()));

        } else {
            menu.findItem(R.id.nav_switch_mode).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_shop).setVisible(false);
            menu.findItem(R.id.nav_my_shopping_cart).setVisible(false);
            menu.findItem(R.id.nav_my_orders).setVisible(false);
            menu.findItem(R.id.nav_loyalty_rewards).setVisible(false);
            menu.findItem(R.id.nav_my_list).setVisible(false);
            menu.findItem(R.id.nav_wallet_reload).setVisible(false);

            loginsignuplayout.setVisibility(View.VISIBLE);
            profileimage.setVisibility(View.GONE);
            profilename.setVisibility(View.GONE);
            settingsimage.setVisibility(View.GONE);
        }
    }

    private void setUpConsumerNavMenu() {
        if (mdb.getConsumerID() == null) {
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_my_orders).setVisible(false);
            menu.findItem(R.id.nav_loyalty_rewards).setVisible(false);
            menu.findItem(R.id.nav_my_list).setVisible(false);
            menu.findItem(R.id.nav_wallet_reload).setVisible(false);

            loginsignuplayout.setVisibility(View.VISIBLE);
            profileimage.setVisibility(View.GONE);
            profilename.setVisibility(View.GONE);
        } else {
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_wallet_reload).setVisible(false);

            loginsignuplayout.setVisibility(View.GONE);
            profilename.setVisibility(View.VISIBLE);
            profileimage.setVisibility(View.VISIBLE);
            profileimage.setOnClickListener(this);

            Glide.with(getViewContext())
                    .load(itemProf.getProfilePicURL())
                    .error(Glide.with(getViewContext())
                            .load(R.drawable.ic_um_default_products))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(RequestOptions.circleCropTransform()
                            .fitCenter()
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                    .into(profileimage);

            String mProfileName = itemProf.getFirstName() + " " + itemProf.getLastName();
            profilename.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Bold.ttf", mProfileName.toUpperCase()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //========================
        //REFRESH BADGE COUNT ICON
        //========================
        setUpBadge();

        if (usertype.equals("CONSUMER")) {
            if (itemProf.getConsumerID() != null && CommonFunctions.isProfPicUpdated) {

                Glide.with(getViewContext())
                        .load(itemProf.getProfilePicURL())
                        .error(Glide.with(getViewContext())
                                .load(R.drawable.ic_um_default_products))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(RequestOptions.circleCropTransform()
                                .fitCenter()
                                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                        .into(profileimage);

                profilename.setText(itemProf.getFirstName() + " " + itemProf.getLastName());
                CommonFunctions.isProfPicUpdated = false;
            }
        } else {
            if (wholesalerProfile.getWholesalerID() != null && CommonFunctions.isProfPicUpdated) {

                Log.d("antonhttp", "MAIN ONRESUME");
                Log.d("antonhttp", "PP URL: " + wholesalerProfile.getProfilePicURL());

                Glide.with(getViewContext())
                        .load(wholesalerProfile.getProfilePicURL())
                        .error(Glide.with(getViewContext())
                                .load(R.drawable.ic_um_default_products))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(RequestOptions.circleCropTransform()
                                .fitCenter()
                                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                .override(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4))
                        .into(profileimage);

            }
        }
    }

    public void displayFrag(int index, Bundle args) {

        switch (index) {
            case 6: {
                //LOYALTY REWARDS
                displayView(5, args);
                break;
            }
            case 5: {
                //WALLET
                displayView(3, args);
                break;
            }
            case 4: {
                //POINTS
                displayView(4, args);
                break;
            }
            case 3: {
                //ORDERS
                displayView(2, args);
                break;
            }
            case 2: {
                //CART
                ViewShoppingCartsActivity.start(getViewContext());
                break;
            }
            default: {
                if (usertype.equals("CONSUMER")) {

                    displayView(index, args);

                } else if (usertype.equals("WHOLESALER")) {

                    if (mdb.getWholesalerID() == null || wholesalerProfile.getWholesalerID().equals(".")) {

                        Intent i = new Intent(getViewContext(), ShoppingModeActivity.class);
                        i.putExtra("islogin", true);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else {

                        displayView(index, args);

                    }

                }
            }
        }
    }

    private void setNavChecked(int id) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(id);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            ShoppingModeActivity.start(getViewContext(), false);
            //openConfirmQuitDialog("Are you sure you want to quit?");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //==================
        //setup badge layout
        //==================
        final MenuItem menuItem = menu.findItem(R.id.action_my_shopping_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        txvCartItemCount = actionView.findViewById(R.id.cart_badge);

        final MenuItem menuNotif = menu.findItem(R.id.action_notification);
        View actionViewNotif = MenuItemCompat.getActionView(menuNotif);
        txvNotificationItemCount = actionViewNotif.findViewById(R.id.notification_badge);

        //======================================
        //apply badge count on cart icon
        //apply badge count on notification icon
        //======================================
        setUpBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_scan_code: {
                checkRuntimeCameraPermission();
                return true;
            }
            case R.id.action_my_shopping_cart: {
                ViewShoppingCartsActivity.start(getViewContext());
                return true;
            }
            case R.id.action_search: {

                return true;
            }
            case R.id.action_notification: {
                UserPreferenceManager.saveIntegerPreference(this, UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);
                NotificationActivity.start(getViewContext());
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //=========================================
    // APPLY BADGE COUNT ON CART ICON
    //========================================
    public void setUpBadge() {
        if (mdb != null) {
            setUpBadgeShoppingCart();
            setUpBadgeNotifiation();
        }
    }

    private void setUpBadgeShoppingCart() {
        shoppingCartSummary = mdb.getShoppingCartSummary(usertype);
        if (txvCartItemCount != null) {
            if (mdb.getAllShoppingCartsQueue().size() == 0) {
                if (txvCartItemCount.getVisibility() != View.GONE) {
                    txvCartItemCount.setVisibility(View.GONE);
                }
            } else {
                mCartItemCount = String.valueOf(mdb.getAllShoppingCartsQueue().size());
                txvCartItemCount.setText(mCartItemCount);
                if (txvCartItemCount.getVisibility() != View.VISIBLE) {
                    txvCartItemCount.setVisibility(View.VISIBLE);
                }
            }
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

    private void getValues() {
        mdb = new UltramegaShopUtilities(getViewContext());
        ConsumerProfile itemProf = mdb.getConsumerProfile();
        imei = CommonFunctions.getIMEI(getViewContext());
        customerid = itemProf.getConsumerID();
        userid = itemProf.getUserID();
        userid = itemProf.getUserID();
        sessionid = UserPreferenceManager.getSession(getViewContext());
        //sessionid = UserPreferenceManager.getSession(getViewContext());
    }

        private void validateQRPartialV2() {

        if(CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            LinkedHashMap<String,String> parameters = new LinkedHashMap<>();
            parameters.put("usertype", "CONSUMER");
            parameters.put("sessionid",sessionid);
            parameters.put("customerid",customerid);
            parameters.put("imei",imei);
            parameters.put("authcode", CommonFunctions.getSha1Hex(imei + userid + sessionid));
            parameters.put("userid", userid);
            parameters.put("limit", "0");

            String checker = new Gson().toJson(parameters);
            Log.d("SPecial", "JSON ipada: "+checker);

            countCart(getCountCartCallback);

        } else {
            Toast.makeText(getViewContext(), "qwe", Toast.LENGTH_SHORT).show();
        }
    }

//    private void countCart(Callback<FetchShoppingCartsQueueResponse> getCountCartCallback) {
//        Call<FetchShoppingCartsQueueResponse> call = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
//                .fetchCountsShoppingCartsQueueCall(
//                        imei,
//                        authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid),
//                        "0",
//                        sessionid,
//                        userid,
//                        customerid,
//                        "CONSUMER");
//        call.enqueue(getCountCartCallback);
//    }
//
//    private Callback<FetchShoppingCartsQueueResponse> getCountCartCallback = new Callback<FetchShoppingCartsQueueResponse>() {
//
//        @Override
//        public void onResponse(@NotNull Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
//            ResponseBody errorBody = response.errorBody();
//            if (errorBody == null) {
//                switch (response.body().getStatus()){
//
//                    case "000":
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(String.valueOf(response.body().getShoppingCartsQueues()));
//                            String count = jsonObject.getString("data");
//                            Log.d("SPecial", "JSON ipada: "+count);
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                        break;
//
////                        if(response.body().getData() != null ){
////                            if(response.body().getData().getParameter_warnings() == null){
////                                //sessionid = UserPreferenceManager.getSession(getActivity());
////                                sessionid = response.body().getSession();
////                                authcode = CommonFunctions.getSha1Hex(imei + sessionid + customerid);
////                                Order order = response.body().getData().getOrder();
////                                Order test = response.body().getData().getParameter_warnings();
////                                String delivery_fee = order.getDelivery_fee_amount();
////                                //String parameter_warnings = order.getParameter_warnings();
////                                Log.d("neil","DELIVERY FEE : "+sessionid);
////                                //Log.d("neil","parameter_warnings : "+parameter_warnings);
////                                delivery_charge.setText(delivery_fee);
////                                UserPreferenceManager.saveDeliveryFee(getViewContext(), delivery_fee);
////                            }
////                            else{
////                                showError("Please select other address");
////                            }
////
//////                            Log.d("Master Tude", "response: " + delivery_fee);
//////                            Log.d("Special", "response: " + response.body().getData());
//////                            Log.d("Special", "responseGetOrder: " + response.body().getData().getOrder());
//////                            Log.d("Special", "responseOrder: " + order);
//////                            Log.d("Special", "responseData: " + response.body().getData());
//////                            Log.d("Special", "responseStatus: " + response.body().getStatus());
//////                            Log.d("Special", "responseMessage: " + response.body().getMessage());
////                        }
////                        break;
////                    case "001":
////                        forceLogoutDialog("Session expired...");
////                        break;
////                    default:
////                        showError(response.body().getMessage());
////                        break;
//
//                }
//
//            } else {
//                showError("Something went wrong. Please try again.");
//            }
//        }
//
//        @Override
//        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
//
//        }
//
//    };




    public static MainActivity getInstance() {
        return instance;
    }

    private void checkRuntimeCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // User may have declined earlier, ask Android if we should show him a reason

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // show an explanation to the user
                // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
            } else {
                // request the permission.
                // CALLBACK_NUMBER is a integer constants
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                // The callback method gets the result of the request.
            }
        } else {

            ScanQRCodeActivity.start(getViewContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAPTURE_IMAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                    Log.d("", "permission granted success");
                    ScanQRCodeActivity.start(getViewContext());

                } else {
                    // permission denied

                    Log.d("", "permission denied");
                    checkRuntimeCameraPermission();
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                switch (item.getItemId()) {
                    case R.id.nav_login: {
                        LoginActivity.start(getViewContext());
                        break;
                    }
                    case R.id.nav_logout: {
                        openLogoutDialog("Are you sure you want to logout?");
                        break;
                    }
                    case R.id.nav_my_shopping_cart: {
                        ViewShoppingCartsActivity.start(getViewContext());
                        break;
                    }
                    case R.id.nav_shop: {
                        displayView(9, null);
                        break;
                    }
                    case R.id.nav_my_orders: {
                        displayView(2, null);
                        break;
                    }
                    case R.id.nav_wallet_reload: {
                        displayView(3, null);
                        break;
                    }
                    case R.id.nav_my_list: {
                        displayView(4, null);
                        break;
                    }
                    case R.id.nav_loyalty_rewards: {
                        displayView(5, null);
                        break;
                    }
                    case R.id.nav_changeEnv:
//                        String[] colors = {"DEVELOPMENT", "STAGING","PRODUCTION"};
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getViewContext());
//                        builder.setTitle("Choose your environment (Debug Mode)");
//                        builder.setItems(colors, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                //truncate tables
//                                mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
//                                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
//                                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
//                                mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
//                                mdb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);
//
//                                UserPreferenceManager.clearPreference(getViewContext());
//
//                                //set notifications
//                                UserPreferenceManager.saveIntegerPreference(getViewContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);
//
//                                //set up login to false
//                                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);
//
//                                if (usertype.equals("CONSUMER")) {
//                                    //consumer
//                                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
//                                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
//                                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
//                                    mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
//
//                                    UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");
//
//                                    MainActivity.start(getViewContext(), 0);
//
//                                } else if (usertype.equals("WHOLESALER")) {
//                                    //wholesaler
//                                    mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);
//
//                                    UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");
//
//                                    finish();
//                                    ShoppingModeActivity.start(getViewContext(), true);
//                                }
//
//                                if(which == 0){
//                                    UserPreferenceManager.saveStringPreference(getViewContext(),UserPreferenceManager.KEY_ENVIRONMENT,RetrofitBuild.DEV_ROOT_URL);
//                                }else if(which == 1){
//                                    UserPreferenceManager.saveStringPreference(getViewContext(),UserPreferenceManager.KEY_ENVIRONMENT,RetrofitBuild.STAGING_ROOT_URL);
//                                }else{
//                                    UserPreferenceManager.saveStringPreference(getViewContext(),UserPreferenceManager.KEY_ENVIRONMENT,RetrofitBuild.PROD_ROOT_URL);
//                                }
//
//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    Intent mStartActivity = new Intent(getViewContext(), SplashScreenActivity.class);
//                                    int mPendingIntentId = 123456;
//                                    PendingIntent mPendingIntent = PendingIntent.getActivity(getViewContext(),
//                                            mPendingIntentId,mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                                    AlarmManager mgr = (AlarmManager)getViewContext().getSystemService(Context.ALARM_SERVICE);
//                                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                                    System.exit(0);
//                                } else {
//                                    ProcessPhoenix.triggerRebirth(getViewContext());
//                                }
//
//
//
//                            }
//                        });
//                        builder.show();

                        break;
                }

            }
        }, 400);
        return true;
    }

    private int mCurrentId = 0;

    public void displayView(int id, Bundle args) {
        Fragment fragment = null;
        String title = "";

        mCurrentId = id;
        setNavChecked(id);

        switch (id) {
            case 0: {
                fragment = new DailyFindsFragment();
                title = getString(R.string.str_daily_finds);
                break;
            }
            case 1: {
                ViewShoppingCartsActivity.start(getViewContext());
                break;
            }
            case 2: {
                fragment = new OrdersFragment();
                title = "Orders";
                break;
            }
            case 3: {
                fragment = new WalletReloadFragment();
                title = "My Wallet";
                break;
            }
            case 4: {
                fragment = new MyListFragment();
                title = "Future Buys";
                break;
            }
            case 5: {
                fragment = new LoyaltyRewardsFragment();
                title = "My Points";
                break;
            }
            case 6: {
                openLogoutDialog("Are you sure you want to logout?");
                break;
            }
            case 7: {
                LoginActivity.start(getViewContext());
                break;
            }
            case 8: {
                Log.d("antonhttp", "IT WENT HERE");
                fragment = new PromosFragment();
                title = "Promos";
                break;
            }
            case 9: {
                fragment = new CategoriesListFragment();
                title = "Categories";
                break;
            }
            case 10: {
                fragment = new SuppliersFragment();
                title = "Suppliers";
                break;
            }
        }

        if (args != null) {
            if (fragment != null) {
                fragment.setArguments(args);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
        } else {
            fragmentTransaction.add(R.id.container_body, fragment);
        }
        fragmentTransaction.commit();

        // set the toolbar title
        setActionBarTitle(title);

    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_settings: {
                SettingsActivity.start(getViewContext());
                break;
            }
            case R.id.profile_image: {
                SettingsActivitySelected.start(getViewContext(), 1);
            }
        }
    }

    public static void switchMode(Context context, int index) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("index", index);
        intent.putExtra("isSwitch", true);
        context.startActivity(intent);
    }

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    public static void start(Context context, int index, Bundle args) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("index", index);
        intent.putExtra("args", args);
        context.startActivity(intent);
    }

    private boolean getConsumerBehavioursPatternLength() {
        return listConsumerPatterns.size() == 0;
    }

    public void interest() {
        if (getConsumerBehavioursPatternLength()) {
            Intent i = new Intent(MainActivity.this, InterestActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem notification = menu.findItem(R.id.action_notification);
        if (UserPreferenceManager.getBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN)) {
            notification.setVisible(true);
        } else {
            notification.setVisible(false);
        }
        return true;
    }



}

