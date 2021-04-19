package com.ultramega.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ultramega.shop.R;
import com.ultramega.shop.base.BaseActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.fragment.myshoppingcart.checkout.pickup.PickUpInstructionsFragment;
import com.ultramega.shop.fragment.myshoppingcart.checkout.pickup.PickUpReviewFragment;
import com.ultramega.shop.fragment.myshoppingcart.checkout.pickup.SetUpPickUpBranchFragment;
import com.ultramega.shop.pojo.Branches;

import java.util.Objects;

public class PickUpActivity extends BaseActivity {

    private int mDisplayedView = 0;
    private String option = "PICKUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(getViewContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        setUpToolBar();

        int mIndexSelected = getIntent().getIntExtra("index", -1);

        Bundle args = null;
        if(getIntent().getStringExtra("option") != null){
             args = new Bundle();
             option = getIntent().getStringExtra("option");
             args.putString("processoption",option);
        }

        displayView(mIndexSelected, args);
    }

    public void displayView(int id, Bundle args) {
        Fragment fragment = null;
        String title = "";
        mDisplayedView = id;
        switch (id) {
            case 1: {
                fragment = new SetUpPickUpBranchFragment();
                //fragment = new PickUpFragment();
                title = "Select Branch";
                break;
            }
            case 2: {
                fragment = new PickUpInstructionsFragment();
                title = "Instructions";
                break;
            }
            case 3: {
                fragment = new PickUpReviewFragment();
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

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, PickUpActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("option","PICKUP");
        context.startActivity(intent);
    }

    public static void startDelivery(Context context, int index,String option) {
        Intent intent = new Intent(context, PickUpActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("option",option);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setBranch(int id, Branches item) {
        Bundle args = new Bundle();
        args.putParcelable("branches", item);
        if(Objects.equals(option, "DELIVERY")) {
            CheckoutProductsActivity.startV2(getViewContext(), 0,item);
        }else{
            displayView(id, args);
        }
    }

    public void setInstructions(int id, Branches item, String instructions) {
        Bundle args = new Bundle();
        args.putParcelable("branches", item);
        args.putString("instructions", instructions);
        //args.putString("pickupschedule", pickupschedule);
        displayView(id, args);
    }
}
