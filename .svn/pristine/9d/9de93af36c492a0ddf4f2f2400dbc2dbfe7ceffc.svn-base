package com.ultramega.shop.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.rohitss.uceh.UCEHandler;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.Category;
import com.ultramega.shop.pojo.ConsumerBehaviourPattern;
import com.ultramega.shop.responses.FetchShopCategoriesResponse;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends BaseActivity {

    private UltramegaShopUtilities mdb;
    private List<ConsumerBehaviourPattern> listConsumerPatterns;

    private int SPLASH_TIME_OUT = 2000;
    private static final int PERMISSIONS_REQUEST = 1001;

    private String pricegroup = "";

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView image_logo = findViewById(R.id.imgLogo);
        final ImageView imgBackground = findViewById(R.id.imgBackground);

        listConsumerPatterns = new ArrayList<>();
        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());


        //for dev purposes
        if(RetrofitBuild.ROOT_URL.contains("http://")){
            new UCEHandler.Builder(this)
                    .setTrackActivitiesEnabled(true)
                    .build();
        }

        if (usertype.equals("CONSUMER")) {
            pricegroup = ".";
            listConsumerPatterns = mdb.getConsumerBehaviourPatterns();
        } else if (usertype.equals("WHOLESALER")) {
            listConsumerPatterns = mdb.getWholesalerBehaviourPatterns();
            if (mdb.getWholesalerID() != null) {
                pricegroup = mdb.getWholesalerProfile().getPriceGroup();
            } else {
                pricegroup = ".";
            }
        }

        /**
         * Apply logo for Splash Screen
         */
        Glide.with(getViewContext())
                .asBitmap()
                .load(R.drawable.ultramega_splash_logo)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        image_logo.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()) / 2, CommonFunctions.getScreenHeight(getViewContext()) / 4);
                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }
                });

        Glide.with(getViewContext())
                .asBitmap()
                .load(R.drawable.ultramega_splash_model)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(new BaseTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        imgBackground.setImageBitmap(resource);
                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {
                        cb.onSizeReady(CommonFunctions.getScreenWidth(getViewContext()), CommonFunctions.getScreenHeight(getViewContext()));
                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }
                });

        /**
         * On a post-Android 6.0 devices, check if the required permissions have
         * been granted.
         */
        validateDestination();

    }

    public void validateDestination(){
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("antonhttp", " SDK INT >= 23");
            checkPermissions();
//            proceedToMain();
        } else {
            Log.d("antonhttp", "ELSE SDK INT >= 23");
            imei = CommonFunctions.getIMEI(getViewContext());
            proceedToMain();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


//
//        if (requestCode == PERMISSIONS_REQUEST) {
//            checkPermissions();
//        }


//        switch (requestCode) {
//            case PERMISSIONS_REQUEST: {
//                Log.d("antonhttp", "\n onRequestPermissionsResult IS CALLED \n");
//                checkPermissions();
//                break;
//            }
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }

    }

    private void initialize() {
        imei = CommonFunctions.getIMEI(getViewContext());
        if (mdb.isCategoriesTableEmpty()) {
            getSession();

        } else {

            Log.d("antonhttp", "PROCEED TO MAIN!!!");

            proceedToMain();
        }
    }

    /**
     * Check if the required permissions have been granted, and
     * {@link #proceedToMain()} if they have. Otherwise
     * {@link #requestPermissions(String[], int)}.
     */
    private void checkPermissions() {

        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }

//        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
//
//        Log.d("antonhttp", "\n UNGRANTED PERMISSION LENGTH: " + String.valueOf(ungrantedPermissions.length) + " \n ");
//
//        if (ungrantedPermissions.length == 0) {
//
//            Log.d("antonhttp", "ZERO UNGRANTED PERMISSIONS!");
//
//            imei = CommonFunctions.getIMEI(getViewContext());
//            if (mdb.isCategoriesTableEmpty()) {
//
//                getSession();
////
//            } else {
//
//                Log.d("antonhttp", "PROCEED TO MAIN!!!");
//
//                proceedToMain();
//            }
//
//        } else {
//
//            Log.d("antonhttp", "UNGRANTED PERMISSIONS!");
//
//            ActivityCompat.requestPermissions(SplashScreenActivity.this, ungrantedPermissions, PERMISSIONS_REQUEST);
//
//        }
    }

    /**
     * Get the list of required permissions by searching the manifest. If you
     * don't think the default behavior is working, then you could try
     * overriding this function to return something like:
     * <p>
     * <pre>
     * <code>
     * return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
     * </code>
     * </pre>
     */
    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    /**
     * Convert the array of required permissions to a {@link Set} to remove
     * redundant elements. Then remove already granted permissions, and return
     * an array of ungranted permissions.
     */
    @TargetApi(23)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext(); ) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                i.remove();
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    private void getSession() {
        if (CommonFunctions.getConnectivityStatus(getViewContext()) > 0) {
            //api session
            createSession(callsession);
        } else {
            opendialog(getString(R.string.generic_internet_error_message));
        }
    }

    private void opendialog(String message) {
        MaterialDialog dialog = new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Ok")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    private final Callback<GetSessionResponse> callsession = new Callback<GetSessionResponse>() {

        @Override
        public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {
                    session = response.body().getSession();
                    getAllCategories();
                } else {
                    opendialog(response.body().getMessage());
                }
            } else {
                //open error dialog message
                opendialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<GetSessionResponse> call, Throwable t) {
            //open error dialog message
            opendialog("Something went wrong with the server. Please try again.");
        }
    };

    private void getAllCategories() {
        Call<FetchShopCategoriesResponse> callcategories = RetrofitBuild.fetchShopCategoriesService(getViewContext())
                .fetchShopCategoriesCall(
                        imei,
                        usertype,
                        CommonFunctions.getSha1Hex(imei + session),
                        "0",
                        session,
                        pricegroup);
        callcategories.enqueue(getAllCategoriesCallback);
    }

    private Callback<FetchShopCategoriesResponse> getAllCategoriesCallback = new Callback<FetchShopCategoriesResponse>() {
        @Override
        public void onResponse(Call<FetchShopCategoriesResponse> call, Response<FetchShopCategoriesResponse> response) {
            ResponseBody errBody = response.errorBody();
            if (errBody == null) {
                if (response.body().getStatus().equals("000")) {

                    mdb.truncateTable(UltramegaShopUtilities.TABLE_CATEGORIES);

                    List<Category> categories = response.body().getCategories();
                    for (Category category : categories) {
                        mdb.insertAllCategories(category);
                    }

                    Log.d("antonhttp", "CATEGORIES PROCEED TO MAIN!!!");

                    proceedToMain();

                } else {
                    opendialog(response.body().getMessage());
                }
            } else {
                //open error dialog message
                opendialog("Something went wrong with the server. Please try again.");
            }

        }

        @Override
        public void onFailure(Call<FetchShopCategoriesResponse> call, Throwable t) {
            //open error dialog message
            opendialog("Something went wrong with the server. Please try again.");
        }
    };

    public void proceedToMain() {
        Handler handler = new Handler(Looper.getMainLooper());
       handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (UserPreferenceManager.getBooleanPreference(getViewContext(), "CODE_VERIFICATION_PAGE")) {
                    finish();
                    String AuthenticationID = UserPreferenceManager.getStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_AUTHENTICATION");
                    String mobilenumber = UserPreferenceManager.getStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_MOBILE_NUMBER");
                    String countrycode = UserPreferenceManager.getStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY_CODE");
                    String country = UserPreferenceManager.getStringPreference(getViewContext(), "CODE_VERIFICATION_PAGE_COUNTRY");
                    ConsumerWholesalerSignUpActivity.setForgotPasswordData(getViewContext(), 3, AuthenticationID, mobilenumber, countrycode, country, "FORGETPASSWORD");
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, ShoppingModeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    private boolean getConsumerBehavioursPatternLength() {
        return listConsumerPatterns.size() > 0;
    }


}
