package com.ultramega.shop.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.ultramega.shop.BuildConfig;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.LoginActivity;
import com.ultramega.shop.activity.MainActivity;
import com.ultramega.shop.activity.ReloadWalletActivity;
import com.ultramega.shop.activity.SearchActivity;
import com.ultramega.shop.activity.ViewMyOrdersDetailsActivity;
import com.ultramega.shop.activity.fullscreenimage.FullScreenActivity;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.activity.wallet.ReloadWalletThruCardActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.common.TextSpannable;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.ImageSlider;
import com.ultramega.shop.pojo.WholesalerProfile;
import com.ultramega.shop.responses.GetSessionResponse;
import com.ultramega.shop.rest.RetrofitBuild;
import com.ultramega.shop.util.CustomTypefaceSpan;
import com.ultramega.shop.util.UserPreferenceManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String Theme_Current = "AppliedTheme";
    private static final String Theme_Consumer = "Consumer_Theme";
    private static final String Theme_Wholesaler = "Wholesaler_Theme";
    protected static final int REQUEST_CAMERA = 0;
    protected static final int SELECT_FILE = 1;
    protected Uri fileUri = null;
    private ProgressDialog mProgressDialog;
    public String sessionid = null;
    private UltramegaShopUtilities mdb;

    public String imei = "";
    public String usertype = "";
    public String session = "";

    private WholesalerProfile wholesalerProfile;
    private ConsumerProfile consumerProfile;

    private MaterialDialog mMaterialDialogError = null;

    protected void setUpToolJustBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }
    }

    public void hideSearchBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout searchBar = (LinearLayout) toolbar.findViewById(R.id.searchbox);
        searchBar.setVisibility(View.GONE);
    }

    public void showSearchBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LinearLayout searchBar = (LinearLayout) toolbar.findViewById(R.id.searchbox);
        if (getCurrentUserType(getViewContext()).equals("CONSUMER")) {

            TextView txvsearch = (TextView) toolbar.findViewById(R.id.textView);
            ImageView imgsearch = (ImageView) toolbar.findViewById(R.id.imageView3);
            txvsearch.setTextColor(ContextCompat.getColor(getViewContext(), R.color.color_9B9B9B));
            imgsearch.setColorFilter(ContextCompat.getColor(getViewContext(), R.color.color_9B9B9B));
            searchBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_box_white));

//            searchBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_box));
        } else if (getCurrentUserType(getViewContext()).equals("WHOLESALER")) {

            TextView txvsearch = (TextView) toolbar.findViewById(R.id.textView);
            ImageView imgsearch = (ImageView) toolbar.findViewById(R.id.imageView3);
            txvsearch.setTextColor(ContextCompat.getColor(getViewContext(), R.color.color_9B9B9B));
            imgsearch.setColorFilter(ContextCompat.getColor(getViewContext(), R.color.color_9B9B9B));
            searchBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_box_white));

        } else {

            searchBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_box));

        }
        searchBar.setVisibility(View.VISIBLE);
        searchBar.setOnClickListener(this);
    }

    protected void setNavHeaderLayout(String selectedMode, NavigationView navigationView) {

        View headerView = navigationView.getHeaderView(0);
        RelativeLayout navLayout = (RelativeLayout) headerView.findViewById(R.id.LinearLayout01);

        switch (selectedMode) {
            case Theme_Consumer: {
                navLayout.setBackgroundResource(R.color.color_0160B0);
                break;
            }
            case Theme_Wholesaler: {
                navLayout.setBackgroundResource(R.color.color_F83832);
                break;
            }
            default: {
                navLayout.setBackgroundResource(R.color.color_0160B0);
                break;
            }
        }

    }

    protected Context getViewContext() {
        return this;
    }

    protected void setAppTheme(Context context) {
        switch (UserPreferenceManager.getStringPreference(context, Theme_Current)) {
            case Theme_Consumer: {
                setTheme(R.style.ThemeApp_Consumer);
                break;
            }
            case Theme_Wholesaler: {
                setTheme(R.style.ThemeApp_Wholesaler);
                break;
            }
            default: {
                setTheme(R.style.ThemeApp_Consumer);
                UserPreferenceManager.saveStringPreference(context, Theme_Current, "Consumer_Theme");
                break;
            }
        }
    }

    public String getCurrentUserType(Context context) {
        switch (UserPreferenceManager.getStringPreference(context, Theme_Current)) {
            case Theme_Consumer: {
                return "CONSUMER";
            }
            case Theme_Wholesaler: {
                return "WHOLESALER";
            }
            default: {
                return "CONSUMER";
            }
        }
    }

    protected void openSelectModeDialog() {

        new MaterialDialog.Builder(getViewContext())
                .title("Select Mode")
                .items(R.array.str_action_profile_picture)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            cameraIntent();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, SELECT_FILE);
                        }
                        return true;
                    }
                })
                .show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        fileUri = getOutputMediaFileUri(1);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        List<ResolveInfo> resolvedIntentActivities = getViewContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            getViewContext().grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Uri getOutputMediaFileUri(int type) {
        Uri photoURI = FileProvider.getUriForFile(getViewContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile(1));

        return photoURI;
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Ultramega-Shop");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Ultramega-Shop", "Oops! Failed create "
                        + "Ultramega-Shop" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void progressDialog(String title, String message) {
        try {
            if (!isFinishing() && mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setInverseBackgroundForced(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage(title + "\n" + message);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void progressDialog() {
        if (!isFinishing() && mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, getString(R.string.progress_title), getString(R.string.progress_message), true);
        }
    }

    public void hideProgressDialog() {
        try {
            if (mProgressDialog != null && !isFinishing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createSession(Callback<GetSessionResponse> sessionCallback) {

        try {

            boolean isLogin = false; //get value from shared prefs
            if (mdb == null) {
                mdb = new UltramegaShopUtilities(getViewContext());
            }

            String userid = "";
            String usertype = "";
            String[] code;
            String mobileno = "";

            usertype = getCurrentUserType(getViewContext());
            if (usertype.equals("CONSUMER")) {
                consumerProfile = mdb.getConsumerProfile();
                isLogin = consumerProfile.getConsumerID() == null;
            } else if (usertype.equals("WHOLESALER")) {
                wholesalerProfile = mdb.getWholesalerProfile();
                isLogin = wholesalerProfile.getWholesalerID() == null || wholesalerProfile.getWholesalerID().equals(".");
            }

            Call<GetSessionResponse> call = null;
            if (isLogin) {
                call = RetrofitBuild.commonApiService(getViewContext())
                        .fetchUnregisteredSessionCall(CommonFunctions.getIMEI(getViewContext()));
            } else {

                if (usertype.equals("CONSUMER")) {
                    code = consumerProfile.getCountryCode().split(":::");
                    userid = consumerProfile.getUserID();
                    mobileno = code[0].concat(consumerProfile.getMobileNo());
                } else if (usertype.equals("WHOLESALER")) {
                    //code = "63";
                    userid = wholesalerProfile.getUserID();
                    mobileno = "63".concat(wholesalerProfile.getMobileNo());
                }

                call = RetrofitBuild.commonApiService(getViewContext())
                        .fetchRegisteredSessionCall(CommonFunctions.getIMEI(getViewContext()),
                                userid,
                                usertype,
                                mobileno);

            }
            call.enqueue(sessionCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void openSuccessfullCartDialog() {
        new MaterialDialog.Builder(getViewContext())
                .content("Item was successfully added to your cart")
                .cancelable(true)
                .positiveText("GO TO CART")
                .neutralText("SHOP AGAIN")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 2);
                        startActivity(intent);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 0);
                        startActivity(intent);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    public void openListDialog() {
        new MaterialDialog.Builder(this)
                .content("Item was successfully added to your list")
                .positiveText("GO TO LIST")
                .neutralText("SHOP AGAIN")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        finish();
//                        ((MainActivity) getViewContext()).displayView(R.id.nav_shop, null);
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 4);
                        startActivity(intent);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        finish();
//                        ((MainActivity) getViewContext()).displayView(R.id.nav_my_list, null);
//                        Intent intent = new Intent(getViewContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("index", 0);
//                        startActivity(intent);
                    }
                })
                .show();
    }

    public void openErrorDialog(String message) {
        try {
            if (mMaterialDialogError == null) {
                mMaterialDialogError = new MaterialDialog.Builder(getViewContext())
                        .content(message)
                        .cancelable(false)
                        .positiveText("OK")
                        .contentColorRes(R.color.color_2C2C2C)
                        .positiveColorRes(R.color.colorAccent)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mMaterialDialogError = null;
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openLogoutDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Proceed")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //truncate tables
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);

                        UserPreferenceManager.clearPreference(getViewContext());

                        //set notifications
                        UserPreferenceManager.saveIntegerPreference(getViewContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);

                        //set up login to false
                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);

                        if (usertype.equals("CONSUMER")) {
                            //consumer
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);

                            UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");

                            MainActivity.start(getViewContext(), 0);

                        } else if (usertype.equals("WHOLESALER")) {
                            //wholesaler
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);

                            UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");

                            finish();
                            ShoppingModeActivity.start(getViewContext(), true);
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();
    }

    public void forceLogoutDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("Close")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //truncate tables
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_SHOPPING_CARTS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_QUEUE);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_ORDERS_HISTORY);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_MY_LIST);
                        mdb.truncateTable(UltramegaShopUtilities.TABLE_NOTIFICATION);

                        UserPreferenceManager.clearPreference(getViewContext());

                        if (usertype.equals("CONSUMER")) {
                            //consumer
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_PROFILE);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_WALLET);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_CONSUMER_ADDRESS);
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_WALLET_RELOAD_QUEUE);

                            UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Consumer_Theme");
                        } else if (usertype.equals("WHOLESALER")) {
                            //wholesaler
                            mdb.truncateTable(UltramegaShopUtilities.TABLE_WHOLESALER_PROFILE);

                            UserPreferenceManager.saveStringPreference(getViewContext(), Theme_Current, "Wholesaler_Theme");
                        }

                        //set notifications
                        UserPreferenceManager.saveIntegerPreference(getViewContext(), UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, 0);

                        //set up login to false
                        UserPreferenceManager.saveBooleanPreference(getViewContext(), UserPreferenceManager.KEY_IS_LOGGED_IN, false);


                        MainActivity.start(getViewContext(), 0);

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();
    }

    public void openLoginDialog(String message) {

        mdb = new UltramegaShopUtilities(getViewContext());

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Proceed")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        LoginActivity.start(getViewContext());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();
    }

    protected void openSuccessfullReload(String message) {
        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText("SHOP NOW")
                .neutralText("GO TO WALLET")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .neutralColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 1);
                        startActivity(intent);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getViewContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("index", 5);
                        startActivity(intent);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    protected void setUpSlider(HashMap<String, String> url_maps, final SliderLayout mDemoSlider) {

        for (String name : url_maps.keySet()) {

            if (!name.isEmpty()) {

                DefaultSliderView defaultSliderView = new DefaultSliderView(getViewContext());

                final String picUrl = url_maps.get(name);

                // initialize a SliderLayout
                // change DefaultSliderView to TextSliderView to show text
                defaultSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                FullScreenActivity.start(getViewContext(), picUrl);
                            }
                        });

                //add your extra information
                defaultSliderView.bundle(new Bundle());
                defaultSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(defaultSliderView);

            }

        }
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.pagerIndicator));
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.setCurrentPosition(0);
        mDemoSlider.stopAutoCycle();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                if (mDemoSlider != null) {

                    mDemoSlider.startAutoCycle();

                }

            }
        }, 3000);

    }

    public void setUpSlider(List<ImageSlider> imageSliderList, final SliderLayout mDemoSlider, final PagerIndicator pagerIndicator) {

        for (ImageSlider imageSlider : imageSliderList) {

            if (imageSlider != null) {

                DefaultSliderView defaultSliderView = new DefaultSliderView(getViewContext());

                final String picUrl = imageSlider.getImagePath();

                // initialize a SliderLayout
                // change DefaultSliderView to TextSliderView to show text
                defaultSliderView
                        .description(imageSlider.getImageName())
                        .image(imageSlider.getImagePath())
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                FullScreenActivity.start(getViewContext(), picUrl);
                            }
                        });

                //add your extra information
                defaultSliderView.bundle(new Bundle());
                defaultSliderView.getBundle()
                        .putString("extra", imageSlider.getImageName());

                mDemoSlider.addSlider(defaultSliderView);

            }

        }
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomIndicator(pagerIndicator);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(10000);
        mDemoSlider.stopAutoCycle();
        mDemoSlider.setCurrentPosition(0);

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mDemoSlider != null) {
                    mDemoSlider.startAutoCycle();
                }
            }
        }, 10000, 10000);

    }

    protected static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }

//                else {
//                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
//                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
//                    tv.setText(text);
//                    tv.setMovementMethod(LinkMovementMethod.getInstance());
//                    tv.setText(
//                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
//                                    viewMore), TextView.BufferType.SPANNABLE);
//                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {

            ssb.setSpan(new TextSpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    protected int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchbox: {
                startActivity(new Intent(getViewContext(), SearchActivity.class));
                break;
            }
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    protected void openReloadWalletDialog(String message) {

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Reload Wallet")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ReloadWalletActivity.start(getViewContext());
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();
    }

    public void openDeleteOrderConfirmationDialog(String message, String positivetext, String neutraltext) {

        new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(false)
                .positiveText(positivetext)
                .negativeText(neutraltext)
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ((ViewMyOrdersDetailsActivity) getViewContext()).deleteIndividualOrder();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

//    public void openCancelWholeOrderDialog(String message, String positivetext, String neutraltext) {
//
//        new MaterialDialog.Builder(getViewContext())
//                .content(message)
//                .cancelable(false)
//                .positiveText(positivetext)
//                .negativeText(neutraltext)
//                .contentColorRes(R.color.color_2C2C2C)
//                .positiveColorRes(R.color.color_FC503F)
//                .negativeColorRes(R.color.color_2C2C2C)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        progressDialog();
//                        createSession(((ViewMyOrdersDetailsActivity) getViewContext()).callCancelOrderSession);
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                })
//                .show();
//    }

    public void showError(String content) {
        MaterialDialog dialog = new MaterialDialog.Builder(getViewContext())
                .positiveText("Ok")
                .content(content)
                .cancelable(false)
                .show();
    }

    public void openConfirmQuitDialog(String message) {
        MaterialDialog dialog = new MaterialDialog.Builder(getViewContext())
                .content(message)
                .cancelable(true)
                .positiveText("Ok")
                .negativeText("Cancel")
                .contentColorRes(R.color.color_2C2C2C)
                .positiveColorRes(R.color.color_FC503F)
                .negativeColorRes(R.color.color_2C2C2C)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public String getMobileNumber(String number) {
        String result = "";
        if (number.length() > 9) {
            if (number.substring(0, 2).equals("63")) {
                if (number.length() == 12) {
                    result = number;
                } else {
                    result = "INVALID";
                }
            } else if (number.substring(0, 2).equals("09")) {
                if (number.length() == 11) {
                    result = "63" + number.substring(1, number.length());
                } else {
                    result = "INVALID";
                }
            } else if (number.substring(0, 1).equals("9")) {
                if (number.length() == 10) {
                    result = "63" + number;
                } else {
                    result = "INVALID";
                }
            } else {
                result = "INVALID";
            }
        } else {
            result = "INVALID";
        }
        return result;
    }

    //=======================================================
    //CALCULATE EXPECTED LIMIT
    //length: size of array list
    //limitSize: limit value for each call
    //=======================================================
    public int getLimit(final double length, final double limitSize) {
        return (int) ((length == 0) ? 0 : limitSize * Math.ceil(length / limitSize));
    }

//    public int getLimit(final int length, final int limitSize) {
//        int m_limit = 0;
//        int x_limit = 0;
//
//        if (length == 0) {
//            m_limit = 0;
//        } else {
//            int index = 1;
//            do {
//                x_limit = limitSize * index;
//                m_limit = x_limit;
//                index = index + 1;
//            } while (length > x_limit);
//        }
//
//        //dispatch to a different thread
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////
////            }
////        }).start();
//
//        return m_limit;
//    }


    public String capitalizeWord(String word) {

        if (word.length() > 0) {

            StringBuilder builder = new StringBuilder();

            try {

                String[] cap_word_arr = word.toLowerCase().split(" ");

                for (String s : cap_word_arr) {
                    String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                    builder.append(cap + " ");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return builder.toString();

        } else {

            return null;

        }

    }

    public void openWalletDialog() {
        new MaterialDialog.Builder(getViewContext())
                .title("Choose where to Reload your Wallet")
                .items("Reload thru Bank", "Reload thru Card")
                .itemsColorRes(R.color.color_0B0B0B)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            ReloadWalletActivity.start(getViewContext());
                        } else {
                            ReloadWalletThruCardActivity.start(getViewContext());
                        }
                        return true;
                    }
                })
                .show();
    }

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    public boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    /**
     * Gets the state of GPS Mode.
     *
     * @return true if enabled.
     */
    public boolean isGPSModeOn() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void setNavFontFace(Menu m) {
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    public void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getViewContext().getAssets(), "fonts/ElliotSans-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

}
