package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.ultramega.shop.R;
import com.ultramega.shop.activity.shoppingmode.ShoppingModeActivity;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.database.UltramegaShopUtilities;
import com.ultramega.shop.fragment.settings.EditAddressFragment;
import com.ultramega.shop.fragment.settings.FactsAndQuestionsFragment;
import com.ultramega.shop.fragment.settings.TermsAndConditionsFragment;
import com.ultramega.shop.fragment.settings.ViewProfileFragment;
import com.ultramega.shop.fragment.settings.changepassword.ChangeUserPasswordFragment;
import com.ultramega.shop.fragment.wholesalerProfile.WholeSalerProfileUpdateFragment;
import com.ultramega.shop.pojo.ConsumerProfile;
import com.ultramega.shop.pojo.WholesalerProfile;

public class SettingsActivitySelected extends BaseActivity {

    private String customerid = "";

    private UltramegaShopUtilities mdb;
    private ConsumerProfile consumerProfile;
    private WholesalerProfile wholesalerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity_selected);
        setUpToolBar();

        mdb = new UltramegaShopUtilities(getViewContext());
        usertype = getCurrentUserType(getViewContext());
        if (usertype.equals("CONSUMER")) {

            consumerProfile = mdb.getConsumerProfile();
            customerid = consumerProfile.getConsumerID();

        } else if (usertype.equals("WHOLESALER")) {

            wholesalerProfile = mdb.getWholesalerProfile();
            customerid = wholesalerProfile.getWholesalerID();

        }

        int mIndexSelected = getIntent().getIntExtra("index", -1);

        try {
            if (customerid != null) {
                setUpLoginFragments(mIndexSelected);
            } else {
                setUpLogoutFragments(mIndexSelected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpLogoutFragments(int mIndexSelected) {
        Fragment fragment = null;
        String title = "";

        switch (mIndexSelected) {
            case 0: {

                finish();
                ShoppingModeActivity.start(getViewContext(), false);

                //fragment = new SwitchShoppingModeFragment();
                //title = "Switch Shopping Mode";

                break;
            }
            case 1: {
                fragment = new TermsAndConditionsFragment();
                title = "Terms and Conditions";
                break;
            }
//            case 2: {
//                fragment = new FactsAndQuestionsFragment();
//                title = "FAQs";
//                break;
//            }
            case 2: {
//                fragment = new FactsAndQuestionsFragment();
//                title = "Quick Tour";
                finish();
                UltramegaQuickTourActivity.start(getViewContext(), "SETTINGS");
                break;
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            setActionBarTitle(title);
        }

    }

    private void setUpLoginFragments(int mIndexSelected) {
        Fragment fragment = null;
        String title = "";

        switch (mIndexSelected) {
            case 0: {

                finish();
                ShoppingModeActivity.start(getViewContext(), false);

                break;
            }
            case 1: {
                try {
                    if (usertype.equals("CONSUMER")) {
                        fragment = new ViewProfileFragment();
                        title = "My Profile";
                        break;
                    } else {
                        fragment = new WholeSalerProfileUpdateFragment();
                        title = "My Profile";
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 2: {
                fragment = new EditAddressFragment();
                title = "My Addresses";
                break;
            }
//            case 3: {
//                fragment = new NotificationsFragment();
//                title = "Notifications";
//                break;
//            }
//            case 4: {
//                fragment = new SupportFragment();
//                title = "Support";
//                break;
//            }
            case 3: {
                fragment = new ChangeUserPasswordFragment();
                title = "Change Password";
                break;
            }
            case 4: {
                fragment = new TermsAndConditionsFragment();
                title = "Terms and Conditions";
                break;
            }
//            case 5: {
//                fragment = new FactsAndQuestionsFragment();
//                title = "FAQs";
//                break;
//            }
            case 5: {
//                fragment = new FactsAndQuestionsFragment();
//                title = "Quick Tour";
                finish();
                UltramegaQuickTourActivity.start(getViewContext(), "SETTINGS");
                break;
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            setActionBarTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
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

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, SettingsActivitySelected.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }
}
