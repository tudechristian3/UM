package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.myshoppingcart.checkout.delivery.AddressFragment;
import com.ultramega.shop.fragment.myshoppingcart.checkout.delivery.ReviewFragment;
import com.ultramega.shop.fragment.myshoppingcart.checkout.delivery.SpecialInstructionsFragment;
import com.ultramega.shop.pojo.Branches;

public class CheckoutProductsActivity extends BaseActivity {

    int mDisplayedView = 0;
    Branches branches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_products);
        setUpToolBar();

        int mIndexSelected = getIntent().getIntExtra("index", -1);

        if(getIntent().getStringExtra("branches") != null){
            branches = new Gson().fromJson(getIntent().getStringExtra("branches"),Branches.class);
        }

        Bundle args = getIntent().getBundleExtra("args");
        displayView(mIndexSelected, args);
    }

    public void displayView(int id, Bundle args) {
        Fragment fragment = null;
        String title = "";
        mDisplayedView = id;

        switch (id) {
            case 0: {
                fragment = new AddressFragment();
                title = "Address";
                break;
            }
            case 1: {
                fragment = new SpecialInstructionsFragment();
                title = "Instructions";
                break;
            }
            case 2: {
                fragment = new ReviewFragment();
                title = "Review";
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

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
    }

    public void setAddress(int id, String address, String latitude, String longitude) {
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);

        displayView(id, args);
    }

    public void setBranch(int id, String branchid) {
        Bundle args = new Bundle();
        args.putString("branch", branchid);
        displayView(id, args);
    }

    public static void setFirstAddress(Context context, int index, String address, String latitude, String longitude) {
        Intent intent = new Intent(context, CheckoutProductsActivity.class);

        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);

        intent.putExtra("args", args);
        intent.putExtra("index", index);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setInstructions(int id, String address, String latitude, String longitude, String instructions) {
        Bundle args = new Bundle();
        args.putString("address", address);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        args.putString("instructions", instructions);
        args.putString("branches",new Gson().toJson(branches));

        displayView(id, args);
    }

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, CheckoutProductsActivity.class);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

    public static void startV2(Context context, int index, Branches branches) {
        Intent intent = new Intent(context, CheckoutProductsActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("branches",new Gson().toJson(branches));
        context.startActivity(intent);
    }

}
