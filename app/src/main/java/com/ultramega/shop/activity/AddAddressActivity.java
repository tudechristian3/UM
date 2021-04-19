package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.WindowManager;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.myshoppingcart.checkout.delivery.ConfirmAddressFragment;
import com.ultramega.shop.fragment.myshoppingcart.checkout.delivery.SetUpAddressFragment;

public class AddAddressActivity extends BaseActivity {

    int mDisplayedView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setUpToolBar();

        int mIndexSelected = getIntent().getIntExtra("index", -1);
        Bundle args = getIntent().getBundleExtra("args");
        displayView(mIndexSelected, args);
    }

    public void displayView(int id, Bundle args) {
        mDisplayedView = id;
        Fragment fragment = null;
        String title = "";
        switch (id) {
            case 1: {
                title = "Add Address";
                fragment = new SetUpAddressFragment();
                break;
            }
            case 2: {
                title = "Confirm Address";
                fragment = new ConfirmAddressFragment();
                break;
            }
        }

        if (args != null) {
            fragment.setArguments(args);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_body, fragment);
            fragmentTransaction.addToBackStack(null);
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

    public void setAddress(int id, Address address, String intentType) {
        Bundle args = new Bundle();
        args.putParcelable("address", address);
        args.putString("intenttype", intentType);
        displayView(id, args);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(CommonFunctions.setTypeFace(getViewContext(), "fonts/ElliotSans-Medium.ttf", title));
    }

    public static void start(Context context, int index, String intentType) {
        Intent intent = new Intent(context, AddAddressActivity.class);
        intent.putExtra("index", index);

        Bundle args = new Bundle();
        args.putString("intenttype", intentType);

        intent.putExtra("args", args);
        context.startActivity(intent);
    }
}
