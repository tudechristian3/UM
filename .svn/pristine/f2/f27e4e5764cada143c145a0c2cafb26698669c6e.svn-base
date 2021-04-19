package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.login.ChangePasswordFragment;
import com.ultramega.shop.fragment.myshoppingcart.LoginWithFacebookFragment;
import com.ultramega.shop.fragment.register.RegisterWholeSalerFragment;
import com.ultramega.shop.fragment.register.SignUpEnterAccessCodeFragment;
import com.ultramega.shop.fragment.register.SignUpEnterMobileFragment;
import com.ultramega.shop.fragment.register.SignUpEnterUserProfileFragment;
import com.ultramega.shop.fragment.register.SetUpWholesalerAddressFragment;

public class ConsumerWholesalerSignUpActivity extends BaseActivity {

    int mDisplayedView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_facebook_sign_up);
        setUpToolBar();

        int mIndexSelected = getIntent().getIntExtra("index", -1);
        if (mIndexSelected == 2) {
            String processtype = getIntent().getStringExtra("processtype");
            Bundle args = new Bundle();
            args.putString("processtype", processtype);
            displayView(mIndexSelected, args);
        } else if (mIndexSelected == 3) {
            String processtype = getIntent().getStringExtra("processtype");
            Bundle args = new Bundle();
            args.putString("AuthenticationID", getIntent().getStringExtra("processtype"));
            args.putString("MobileNumber", getIntent().getStringExtra("processtype"));
            args.putString("CountryCode", getIntent().getStringExtra("processtype"));
            args.putString("Country", getIntent().getStringExtra("processtype"));
            args.putString("processtype", processtype);
            displayView(mIndexSelected, args);
        } else {
            displayView(mIndexSelected, null);
        }

    }

    public void displayView(int id, Bundle args) {
        mDisplayedView = id;
        Fragment fragment = null;
        String title = "";
        switch (id) {
            case 1: {
                fragment = new LoginWithFacebookFragment();
                title = "Login with Facebook";
                break;
            }
            case 2: {
                fragment = new SignUpEnterMobileFragment();
                title = "Mobile";
                break;
            }
            case 3: {
                fragment = new SignUpEnterAccessCodeFragment();
                title = "Access Code";
                break;
            }
            case 4: {
                fragment = new SignUpEnterUserProfileFragment();
                title = "Profile";
                break;
            }
            case 5: {
                //fragment = new RegisterWholeSalerMapFragment();
                fragment = new SetUpWholesalerAddressFragment();
                title = "Profile";
                break;
            }
            case 6: {
                fragment = new RegisterWholeSalerFragment();
                title = "Profile";
                break;
            }
            case 7: {
                fragment = new ChangePasswordFragment();
                title = "Forgot Password";
                break;
            }
        }

        if (args != null) {
            fragment.setArguments(args);
        }

        if (fragment != null) {
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
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public void setAddress(int id, String MobileNumber, String CountryCode, String Country, Address address) {
        Bundle args = new Bundle();
        args.putString("MobileNumber", MobileNumber);
        args.putString("CountryCode", CountryCode);
        args.putString("Country", Country);
        args.putParcelable("address", address);

        displayView(id, args);
    }

    public void setFinalRegistration(int id, String MobileNumber, String CountryCode, String Country) {
        Bundle args = new Bundle();
        args.putString("MobileNumber", MobileNumber);
        args.putString("CountryCode", CountryCode);
        args.putString("Country", Country);

        displayView(id, args);
    }

    public void setPartialRegistration(int id, String AuthenticationID, String MobileNumber, String CountryCode, String Country, String processtype) {
        Bundle args = new Bundle();
        args.putString("AuthenticationID", AuthenticationID);
        args.putString("MobileNumber", MobileNumber);
        args.putString("CountryCode", CountryCode);
        args.putString("Country", Country);
        args.putString("processtype", processtype);

        displayView(id, args);
    }

    public void setActionBarTitle(String title) {
        setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
    }

//    @Override
//    public void onBackPressed() {
//        if (mDisplayedView == 1) {
//            super.onBackPressed();
//        } else if (mDisplayedView == 2) {
//            displayView(1, null);
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, ConsumerWholesalerSignUpActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    public static void verifyMobile(Context context, int index, String processtype) {
        Intent intent = new Intent(context, ConsumerWholesalerSignUpActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("processtype", processtype);
        context.startActivity(intent);
    }

    public static void setForgotPasswordData(Context context, int index, String AuthenticationID, String MobileNumber, String CountryCode, String Country, String processtype) {
        Intent intent = new Intent(context, ConsumerWholesalerSignUpActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("AuthenticationID", AuthenticationID);
        intent.putExtra("MobileNumber", MobileNumber);
        intent.putExtra("CountryCode", CountryCode);
        intent.putExtra("Country", Country);
        intent.putExtra("processtype", processtype);
        context.startActivity(intent);
    }

}
