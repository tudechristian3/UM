package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.ultramega.shop.R;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.appintro.AppIntroCommonFragment;
import com.ultramega.shop.util.UserPreferenceManager;

/**
 * Created by Ban_Lenovo on 9/25/2017.
 */

public class UltramegaQuickTourActivity extends AppIntro2 {

    protected static final String Theme_Current = "AppliedTheme";
    private static final String Theme_Consumer = "Consumer_Theme";
    private static final String Theme_Wholesaler = "Wholesaler_Theme";
    private UltramegaShopUtilities mdb;
    private String usertype = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        switch (UserPreferenceManager.getStringPreference(this, Theme_Current)) {
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
                break;
            }
        }
        super.onCreate(savedInstanceState);

        mdb = new UltramegaShopUtilities(this);
        usertype = getIntent().getStringExtra("usertype");

        addSlide(AppIntroCommonFragment.newInstance(AppIntroCommonFragment.ID_1));
        addSlide(AppIntroCommonFragment.newInstance(AppIntroCommonFragment.ID_2));
        addSlide(AppIntroCommonFragment.newInstance(AppIntroCommonFragment.ID_3));
        addSlide(AppIntroCommonFragment.newInstance(AppIntroCommonFragment.ID_4));
        addSlide(AppIntroCommonFragment.newInstance(AppIntroCommonFragment.ID_5));

        showStatusBar(false);
        setNavBarColor(R.color.transparent);
        showSkipButton(false);
        showPagerIndicator(true);


//        // Animations -- use only one of the below. Using both could cause errors.
//        setFadeAnimation(); // OR
//        setZoomAnimation(); // OR
//        setFlowAnimation(); // OR
//        setSlideOverAnimation(); // OR
        setDepthAnimation(); // OR


    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.

        if (usertype.equals("SETTINGS")) {
            finish();
        } else {
            if (usertype.equals("CONSUMER")) {
                UserPreferenceManager.saveBooleanPreference(this, UserPreferenceManager.KEY_IS_FIRST_LOGIN_SHOP, true);
            } else if (usertype.equals("WHOLESALER")) {
                UserPreferenceManager.saveBooleanPreference(this, UserPreferenceManager.KEY_IS_FIRST_LOGIN_WHOLESALER, true);
            }

            finish();

            if (mdb.getAllShoppingCartsQueue().size() > 0) {

                MainActivity.start(this, 2);

            } else {
                MainActivity.start(this, 0);
            }
        }

//        if (usertype.equals("CONSUMER")) {
//            UserPreferenceManager.saveBooleanPreference(this, UserPreferenceManager.KEY_IS_FIRST_LOGIN_SHOP, true);
//        } else if (usertype.equals("WHOLESALER")) {
//            UserPreferenceManager.saveBooleanPreference(this, UserPreferenceManager.KEY_IS_FIRST_LOGIN_WHOLESALER, true);
//        } else if (usertype.equals("SETTINGS")) {
//            finish();
//        } else {
//            finish();
//
//            if (mdb.getAllShoppingCartsQueue().size() > 0) {
//
//                MainActivity.start(this, 2);
//
//            } else {
//                MainActivity.start(this, 0);
//            }
//        }

    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }

    public static void start(Context context, String usertype) {
        Intent intent = new Intent(context, UltramegaQuickTourActivity.class);
        intent.putExtra("usertype", usertype);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usertype.equals("SETTINGS")) {
            finish();
        } else {
            MainActivity.start(this, 0);
        }

    }
}
