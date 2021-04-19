package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.ProfileTracker;
//import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.OrderSkus;
import com.ultramega.shop.pojo.ShoppingCartsQueue;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.FetchShoppingCartsQueueResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.responses.LoginResponse;
import com.ultramega.shop.responses.LoginWholesalerResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private MaterialEditText edtmobilenumber;
    private MaterialEditText edtpassword;

    private String customerid = "";
    private String sessionid = "";
    private String authcode = "";
    private String imei = "";
    private String userid = "";
    private String password = "";
    private String orderskus = "";
    private String forgotpassword = "";

    private List<ConsumerProfile> listprofile;
    private List<OrderSkus> listorders;
    private List<ShoppingCartsQueue> listqueue;

    private UltramegaShopUtilities mdb;
    private String usertype = "";
    private int limit = 0;
    private boolean isloadMore = false;

    //    private LoginButton loginButton;
//    private CallbackManager callbackManager;
//    private AccessTokenTracker accessTokenTracker;
//    private ProfileTracker profileTracker;
//    private ShareDialog shareDialog;

//    //Facebook login button
//    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            Profile profile = Profile.getCurrentProfile();
//            nextActivity(profile);
//        }
//
//        @Override
//        public void onCancel() {
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        setUpToolBar();

//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("public_profile email");
//        loginButton.setVisibility(View.GONE);

        init();

//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
//            }
//        };
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
//            }
//        };
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();

//        callback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
//                nextActivity(profile);
//            }
//
//            @Override
//            public void onCancel() {
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//            }
//        };
//        loginButton.registerCallback(callbackManager, callback);

        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
//                nextActivity(profile);
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        AccessToken accessToken = loginResult.getAccessToken();
//                        Profile profile = Profile.getCurrentProfile();
//                        nextActivity(profile);
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });

    }

    private void init() {

        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Log In"));

        //initialize empty data
        String loginText = "";
        mdb = new UltramegaShopUtilities(getViewContext());
        imei = CommonFunctions.getIMEI(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {
            customerid = mdb.getConsumerID();
            loginText = "Log In as Consumer";
        } else if (usertype.equals("WHOLESALER")) {
            customerid = mdb.getWholesalerID();
            loginText = "Log In as Wholesaler";
        }
        //finishes the activity if already login
        if (customerid != null && !customerid.equals(".")) {
            finish();
        }

        listorders = new ArrayList<>();
        listqueue = new ArrayList<>();
        listprofile = new ArrayList<>();

        edtmobilenumber = (MaterialEditText) findViewById(R.id.edtmobilenumber);
        edtpassword = (MaterialEditText) findViewById(R.id.edtpassword);
        TextView txv_label_one_or = (TextView) findViewById(R.id.txv_one_label_or);
        txv_label_one_or.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "or"));
        TextView txv_account_label = (TextView) findViewById(R.id.txv_account_label);
        txv_account_label.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Does not have an account yet?"));

        TextView forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        forgotpassword.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Forgot Password?"));
        forgotpassword.setOnClickListener(this);

        Button button_login = (Button) findViewById(R.id.btnLogin);
        button_login.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", loginText));
        button_login.setOnClickListener(this);

        TextView signup = (TextView) findViewById(R.id.btnSignUp);
        signup.setText(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", "Sign Up"));
        signup.setOnClickListener(this);

        if (usertype.equals("CONSUMER")) {
            txv_label_one_or.setVisibility(View.GONE);
//            txv_label_one_or.setVisibility(View.VISIBLE);
        } else if (usertype.equals("WHOLESALER")) {
            txv_label_one_or.setVisibility(View.GONE);
//            loginButton.setVisibility(View.GONE);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //Facebook login
////        Profile profile = Profile.getCurrentProfile();
////        nextActivity(profile);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        //Facebook login
////        accessTokenTracker.stopTracking();
////        profileTracker.stopTracking();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }

//    private void nextActivity(Profile profile) {
//        if (profile != null) {
//            Log.d("antonhttp", new Gson().toJson(profile));
//            Log.d("antonhttp", profile.getProfilePictureUri(200, 200).toString());
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startClearStack(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnLoginWithFacebook: {
//                ConsumerWholesalerSignUpActivity.start(getViewContext(), 1);
//                break;
//            }
            case R.id.btnSignUp: {
                //finish();
                ConsumerWholesalerSignUpActivity.verifyMobile(getViewContext(), 2, ".");
                break;
            }
            case R.id.btnLogin: {
                if (evaluateLoginForm()) {
                    String mobileno = getMobileNumber(edtmobilenumber.getText().toString().trim());
                    if (mobileno.equals("INVALID")) {
                        openErrorDialog("Invalid Mobile Number");
                    } else {
                        userid = mobileno;
                        password = edtpassword.getText().toString().trim();

                        progressDialog("Logging in...", "Please wait.");

                        //api session
                        createSession(callsession);
                    }
                } else {
                    openErrorDialog("All fields are required");
                }

                break;
            }
            case R.id.forgotpassword: {
                ConsumerWholesalerSignUpActivity.verifyMobile(getViewContext(), 2, "FORGETPASSWORD");
                break;
            }
        }
    }

    private boolean evaluateLoginForm() {
        return edtmobilenumber.getText().toString().trim().length() > 0 && edtpassword.getText().toString().trim().length() > 0;
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    UserPreferenceManager.saveSession(getApplication(), sessionid);
                    Log.d("Login", "sessionid: "+sessionid);
                    Log.d("Login", "sharepref: "+UserPreferenceManager.getSession(getApplication()));

                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    if (usertype.equals("CONSUMER")) {
                        UserPreferenceManager.saveSession(getApplication(), sessionid);
                        listqueue = mdb.getAllShoppingCartsQueue();

                        for (int i = 0; i < listqueue.size(); i++) {
                            listorders.add(new OrderSkus(
                                    listqueue.get(i).getItemCode(),
                                    listqueue.get(i).getCatClass(),
                                    listqueue.get(i).getQuantity(),
                                    listqueue.get(i).getPackageCode(),
                                    listqueue.get(i).getItemPicUrl()));
                        }
                        orderskus = new Gson().toJson(listorders);

                        login(loginSession);
                    } else if (usertype.equals("WHOLESALER")) {
                        loginWholeSaler(loginWholesalerSession);
                    }

                } else {
                    hideProgressDialog();
                    showError(response.body().getMessage());
                }
            } else {
                hideProgressDialog();
                showError("Something went wrong with the server. Please contact support.");
            }

        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong with the server. Please contact support.");
        }
    };


    private void login(Callback<LoginResponse> loginCallback) {
        String tokenFCM = FirebaseInstanceId.getInstance().getToken();
        UserPreferenceManager.saveSession(getApplication(), sessionid);
        if (tokenFCM == null) {
            hideProgressDialog();
            showError("Invalid Firebase Token.");
        } else {
            Call<LoginResponse> login = RetrofitBuild.loginService(getViewContext())
                    .loginCall(userid,
                            authcode,
                            imei,
                            sessionid,
                            password,
                            orderskus,
                            tokenFCM);
            login.enqueue(loginCallback);
        }

    }

    private void loginWholeSaler(Callback<LoginWholesalerResponse> loginWholeSalerCallback) {
        String tokenFCM = FirebaseInstanceId.getInstance().getToken();

        if (tokenFCM == null) {
            hideProgressDialog();
            showError("Invalid Firebase Token.");
        } else {
            Call<LoginWholesalerResponse> login = RetrofitBuild.loginService(getViewContext())
                    .loginWholeSalerCall(userid,
                            authcode,
                            imei,
                            sessionid,
                            password,
                            tokenFCM);
            login.enqueue(loginWholeSalerCallback);
        }

    }

    private final Callback<LoginResponse> loginSession = new Callback<LoginResponse>() {

        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateResponse(response);
            } else {
                showError("Something went wrong with the server. Please contact support.");
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong with the server. Please contact support.");
        }
    };

    private final Callback<LoginWholesalerResponse> loginWholesalerSession = new Callback<LoginWholesalerResponse>() {

        @Override
        public void onResponse(Call<LoginWholesalerResponse> call, Response<LoginWholesalerResponse> response) {
            hideProgressDialog();
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                evaluateWholesalerResponse(response);
            } else {
                showError("Something went wrong with the server. Please contact support.");
            }
        }

        @Override
        public void onFailure(Call<LoginWholesalerResponse> call, Throwable t) {
            hideProgressDialog();
            showError("Something went wrong with the server. Please contact support.");
        }
    };

    private void evaluateWholesalerResponse(Response<LoginWholesalerResponse> response) {
        switch (response.body().getStatus()) {
            case "000": {

                mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDER_PAYMENTS);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);

                List<WholesalerProfile> listwholesalerprofile = response.body().getListprofile();

                if (listwholesalerprofile.get(0).getWholesalerID() == null) {
                    showError("Account does not exist");
                } else {

                    for (WholesalerProfile profile : listwholesalerprofile) {
                        mdb.insertWholesalerProfile(profile);
                    }

                    Bundle args = new Bundle();
                    args.putBoolean("isShow", true);

                    //set up login to true
                    UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
                    finish();
                    TaskStackBuilder.create(getViewContext()).addNextIntent(new Intent(getViewContext(), MainActivity.class).putExtra("args", args)).addNextIntent(getIntent()).startActivities();
                }

                break;
            }
            default: {
                openErrorDialog(response.body().getMessage());
                break;
            }
        }
    }

    private void evaluateResponse(Response<LoginResponse> response) {

        switch (response.body().getStatus()) {
            case "000": {
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDER_PAYMENTS);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);

                listprofile = response.body().getListprofile();
                for (ConsumerProfile profile : listprofile) {
                    mdb.insertConsumerProfile(profile);
                }

                //get shopping carts queue
                createSession(cartsqueuesession);

                //set up login to true
                UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, true);
                finish();
//                if (UserPreferenceManager.getBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_FIRST_LOGIN_SHOP)) {
                TaskStackBuilder.create(getViewContext()).addNextIntent(new Intent(getViewContext(), MainActivity.class)).addNextIntent(getIntent()).startActivities();
//                } else {
//                    UltramegaQuickTourActivity.start(getViewContext(), "CONSUMER");
//                }
                break;
            }
            default: {
                openErrorDialog(response.body().getMessage());
            }
        }
    }

    private final Callback<GetSessionResponse> cartsqueuesession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    sessionid = response.body().getSession();
                    authcode = CommonFunctions.getSha1Hex(imei + userid + sessionid);

                    //fetchShoppingCart();
                    fetchShoppingCarts(callshoppingsession);
                }
            }
        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {

        }
    };

//    private void fetchShoppingCart() {
//        if(isloadMore){
//            fetchShoppingCarts(callshoppingsessionLoadMore);
//        }else{
//            fetchShoppingCarts(callshoppingsession);
//        }
//    }

    private void fetchShoppingCarts(Callback<FetchShoppingCartsQueueResponse> getshopqueueCallback) {
        Call<FetchShoppingCartsQueueResponse> shopqueue = RetrofitBuild.fetchShoppingCartsQueueService(getViewContext())
                .fetchShoppingCartsQueueCall(imei,
                        authcode,
                        "0",
                        sessionid,
                        userid,
                        listprofile.get(0).getConsumerID(),
                        getCurrentUserType(getViewContext()));
        shopqueue.enqueue(getshopqueueCallback);
    }

    private final Callback<FetchShoppingCartsQueueResponse> callshoppingsession = new Callback<FetchShoppingCartsQueueResponse>() {

        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                    List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
//                    if(shoppingqueue.size() > 0){
//                        isloadMore = true;
                        for (ShoppingCartsQueue queue : shoppingqueue) {
                            mdb.insertAllShoppingCartsQueue(queue);
                        }
                        //fetchShoppingCart();
//                    }else{
//                        isloadMore = false;
//                    }
                }
            }
        }

        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };
    private final Callback<FetchShoppingCartsQueueResponse> callshoppingsessionLoadMore = new Callback<FetchShoppingCartsQueueResponse>() {

        @Override
        public void onResponse(Call<FetchShoppingCartsQueueResponse> call, Response<FetchShoppingCartsQueueResponse> response) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                if (response.body().getStatus().equals("000")) {
                    List<ShoppingCartsQueue> shoppingqueue = response.body().getShoppingCartsQueues();
                    //if(shoppingqueue.size() > 0){
                        //isloadMore = true;
                        for (ShoppingCartsQueue queue : shoppingqueue) {
                            mdb.insertAllShoppingCartsQueue(queue); }

                        //fetchShoppingCart();

//                    }else{
//                        isloadMore = false;
//                    }
                }
            }
        }

        @Override
        public void onFailure(Call<FetchShoppingCartsQueueResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

}
